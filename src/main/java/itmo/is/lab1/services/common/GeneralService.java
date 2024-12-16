package itmo.is.lab1.services.common;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.GeneralRepository;
import itmo.is.lab1.services.common.errors.BadRequestException;
import itmo.is.lab1.services.common.errors.ForbiddenException;
import itmo.is.lab1.services.common.errors.NotFoundException;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.user.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class GeneralService<T extends GeneralEntityRequest, R extends GeneralEntityResponse, E extends GeneralEntity<T>> {
    @Autowired
    protected GeneralRepository<E> repository;
    @Autowired
    protected UserService userService;

    public List<R> findAll() {
        return repository.findAll()
                .stream().map(this::buildResponse)
                .collect(Collectors.toList());
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
        E entity = getEntityById(id);
        repository.delete(entity);
        return new GeneralMessageResponse()
                .setMessage("Объект успешно удалён.");
    }


    public R create(@NotNull T request) {
        if (!request.isValid()) {
            throw new BadRequestException();
        }

        E entity = buildEntity(request);
        repository.save(entity);
        return buildResponse(entity);
    }

    protected abstract E buildEntity(T request);

    public R updateById(@NotNull Long id, @NotNull T request) {
        E entity = getEntityById(id);
        entity.setValues(request, entity.getUser());
        repository.save(entity);
        return buildResponse(entity);
    }

    protected E getEntityById(Long id) {
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
