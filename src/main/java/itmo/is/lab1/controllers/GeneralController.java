package itmo.is.lab1.controllers;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class GeneralController<T extends GeneralEntityRequest,
        R extends GeneralEntityResponse,
        E extends GeneralEntity<T>,
        L extends GeneralService<T, R, E>> {

    @Autowired
    private L service;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public R create(@RequestBody T request) {
        return service.create(request);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public @NotNull List<R> findAll() {
        return service.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull R findById(@PathVariable @NotNull Long id) {
        return service.findById(id);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull GeneralMessageResponse deleteById(@PathVariable @NotNull Long id) {
        return service.deleteById(id);
    }

    @PutMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull R changeById(@PathVariable @NotNull Long id, @RequestBody T request) {
        return service.updateById(id, request);
    }
}
