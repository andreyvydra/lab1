package itmo.is.lab1.services.common;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.GeneralRepository;
import itmo.is.lab1.services.common.errors.ForbiddenException;
import itmo.is.lab1.services.common.errors.NotFoundException;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.user.UserService;
import itmo.is.lab1.specification.GeneralSpecification;
import itmo.is.lab1.specification.PaginatedResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class GeneralService<
        T extends GeneralEntityRequest,
        R extends GeneralEntityResponse,
        E extends GeneralEntity<T>,
        P extends GeneralRepository<E>
        > {
    @Autowired
    protected P repository;
    @Autowired
    protected UserService userService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public PaginatedResponse<R> findAll(String filter, String sortField, Boolean ascending, Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                ascending ? Sort.by(sortField).ascending() : Sort.by(sortField).descending()
        );

        Page<E> resultPage = repository.findAll(
                GeneralSpecification.filterByMultipleFields(filter), pageRequest);

        List<R> content = resultPage.getContent().stream()
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

    protected abstract R buildResponse(E element);

    public R findById(Long id) {
        Optional<E> location = repository.findById(id);
        if (location.isPresent()) {
            return buildResponse(location.get());
        }
        throw new NotFoundException();
    }


    public GeneralMessageResponse deleteById(@NotNull Long id) {
        E entity = getOwnedEntityById(id);
        repository.delete(entity);
        messagingTemplate.convertAndSend("/topic/entities", id);
        return new GeneralMessageResponse()
                .setMessage("Объект успешно удалён.");
    }


    public R create(@NotNull T request) {
        E entity = buildEntity(request);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    protected abstract E buildEntity(T request);

    public R updateById(@NotNull Long id, @NotNull T request) {
        E entity = getOwnedEntityById(id);
        entity.setValues(request, entity.getUser());
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    protected E getOwnedEntityById(Long id) {
        Optional<E> entityOpt = repository.findById(id);
        E entity = null;
        if (entityOpt.isPresent()) {
            entity = entityOpt.get();
        }

        if (entity == null) {
            throw new NotFoundException();
        }

        if (!(entity.getIsChangeable() && userService.getCurrentUser().getRole() == Role.ROLE_ADMIN)
                && entity.getUser() != userService.getCurrentUser()) {
            throw new ForbiddenException();
        }
        return entity;
    }

}
