package itmo.is.lab1.controllers;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.specification.PaginatedResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class GeneralController<T extends GeneralEntityRequest,
        R extends GeneralEntityResponse,
        E extends GeneralEntity<T>,
        L extends GeneralService<T, R, E>> {

    @Autowired
    private L service;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public @NotNull R create(@Validated @RequestBody T request) {
        return service.create(request);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public @NotNull PaginatedResponse<R> findAll(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "name") String sortField,
            @RequestParam(required = false, defaultValue = "true") Boolean ascending,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        return service.findAll(filter, sortField, ascending, page, size);
    }

    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull R findById(@PathVariable @NotNull Long id) {
        return service.findById(id);
    }

    @DeleteMapping(
            value = "/{id}/delete",
            produces = APPLICATION_JSON_VALUE)
    public @Valid GeneralMessageResponse deleteById(@PathVariable @NotNull Long id) {
        return service.deleteById(id);
    }

    @PutMapping(
            value = "/{id}/change",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull R changeById(@PathVariable @NotNull Long id, @Validated @RequestBody T request) {
        return service.updateById(id, request);
    }
}
