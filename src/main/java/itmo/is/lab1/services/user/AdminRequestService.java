package itmo.is.lab1.services.user;

import itmo.is.lab1.models.user.AdminRequest;
import itmo.is.lab1.models.user.AdminRequestStatus;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.AdminRequestRepository;
import itmo.is.lab1.services.common.errors.ForbiddenException;
import itmo.is.lab1.services.exceptions.AdminRequestAlreadyProcessedException;
import itmo.is.lab1.services.exceptions.AdminRequestNotFoundException;
import itmo.is.lab1.services.user.responses.AdminRequestResponse;
import itmo.is.lab1.specification.GeneralSpecification;
import itmo.is.lab1.specification.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminRequestService {

    private final AdminRequestRepository adminRequestRepository;
    private final UserService userService;

    @Transactional
    public AdminRequestResponse createAdminRequest(User user) {
        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }

        AdminRequest request = AdminRequest.builder()
                .user(user)
                .status(AdminRequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        AdminRequest savedRequest = adminRequestRepository.save(request);
        return buildResponse(savedRequest);
    }

    public PaginatedResponse<AdminRequestResponse> getAllAdminRequests(String sortField, Boolean ascending, Integer page, Integer size) {
        if (!userService.getCurrentUser().getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                ascending ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
        );

        Page<AdminRequest> resultPage = adminRequestRepository.findAll(pageRequest);

        List<AdminRequestResponse> content = resultPage.getContent().stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                content,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize()
        );
    }

    public PaginatedResponse<AdminRequestResponse> getAdminRequestsByStatus(AdminRequestStatus status, String sortField, Boolean ascending, Integer page, Integer size) {
        if (!userService.getCurrentUser().getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }

        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                ascending ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
        );

        Page<AdminRequest> resultPage = adminRequestRepository.findByStatus(status, pageRequest);

        List<AdminRequestResponse> content = resultPage.getContent().stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                content,
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize()
        );
    }

    private AdminRequestResponse buildResponse(AdminRequest request) {
        return AdminRequestResponse.builder()
                .id(request.getId())
                .userId(request.getUser().getId())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .reviewedByUserId(request.getReviewedBy() != null ? request.getReviewedBy().getId() : null)
                .build();
    }

    public void changeStatus(Long id, User admin, AdminRequestStatus adminRequestStatus) {
        if (!userService.getCurrentUser().getRole().equals(Role.ROLE_ADMIN)) {
            throw new ForbiddenException();
        }

        AdminRequest request = adminRequestRepository.findById(id)
                .orElseThrow(() -> new AdminRequestNotFoundException("Заявка не найдена"));

        if (request.getStatus() != AdminRequestStatus.PENDING) {
            throw new AdminRequestAlreadyProcessedException("Заявка уже обработана");
        }

        request.setStatus(adminRequestStatus);
        request.setReviewedBy(admin);
        request.getUser().setRole(Role.ROLE_ADMIN);

        adminRequestRepository.save(request);
    }
}