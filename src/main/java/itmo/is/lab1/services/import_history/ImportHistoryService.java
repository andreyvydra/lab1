package itmo.is.lab1.services.import_history;

import itmo.is.lab1.models.ImportHistory;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.ImportHistoryRepository;
import itmo.is.lab1.services.import_history.responses.ImportHistoryResponse;
import itmo.is.lab1.services.user.UserService;
import itmo.is.lab1.specification.GeneralSpecification;
import itmo.is.lab1.specification.PaginatedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ImportHistoryService {

    @Autowired
    private ImportHistoryRepository importHistoryRepository;

    @Autowired
    private UserService userService;

    public ImportHistory getById(Long id) {
        return importHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Import history not found: " + id));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ImportHistory startImport(User user) {
        ImportHistory history = new ImportHistory();
        history.setUser(user);
        history.setStartTime(LocalDateTime.now());
        history.setStatus("IN_PROGRESS");

        return importHistoryRepository.save(history);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finishImport(ImportHistory history, String status, Integer addedObjects, String originalFileName, String objectKey) {
        history.setEndTime(LocalDateTime.now());
        history.setStatus(status);
        history.setAddedObjects(addedObjects);
        history.setOriginalFileName(originalFileName);
        history.setObjectKey(objectKey);
        importHistoryRepository.save(history);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PaginatedResponse<ImportHistoryResponse> getAllHistory(String filter, String sortField, Boolean ascending, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                ascending ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
        );

        Specification<ImportHistory> filterSpec = GeneralSpecification.filterByMultipleFields(filter);
        Specification<ImportHistory> spec;
        if (userService.getCurrentUser().getRole().equals(Role.ROLE_ADMIN)) {
            spec = filterSpec;
        } else {
            User current = userService.getCurrentUser();
            Specification<ImportHistory> userSpec =
                    (root, query, cb) -> cb.equal(root.get("user"), current);
            spec = filterSpec.and(userSpec);
        }

        Page<ImportHistory> resultPage = importHistoryRepository.findAll(spec, pageRequest);

        List<ImportHistoryResponse> content = resultPage.stream().map(this::buildResponse).collect(Collectors.toList());

        return new PaginatedResponse<>(
                content,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize()
        );

    }

    public ImportHistoryResponse buildResponse(ImportHistory history) {
        return new ImportHistoryResponse()
                .setId(history.getId())
                .setStatus(history.getStatus())
                .setAddedObjects(history.getAddedObjects())
                .setStartTime(history.getStartTime())
                .setEndTime(history.getEndTime())
                .setUser(history.getUser().getId())
                .setOriginalFileName(history.getOriginalFileName())
                .setDownloadUrl(history.getObjectKey() == null ? null : "/api/import-history/" + history.getId() + "/file");
    }
}
