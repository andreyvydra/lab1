package itmo.is.lab1.controllers;

import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationResponse;
import itmo.is.lab1.services.location.LocationService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/location")
public class LocationController {
    @Autowired
    private LocationService locationService;

    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public LocationResponse create(@RequestBody LocationRequest request) {
        return locationService.createLocation(request);
    }

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public @NotNull List<LocationResponse> findAll() {
        return locationService.findAll();
    }

    @GetMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull LocationResponse findById(@PathVariable @NotNull Long id) {
        return locationService.findById(id);
    }

    @DeleteMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull LocationResponse deleteById(@PathVariable @NotNull Long id) {
        return locationService.deleteById(id);
    }

    @PutMapping(
            value = "/{id}",
            produces = APPLICATION_JSON_VALUE)
    public @NotNull LocationResponse changeById(@PathVariable @NotNull Long id, @RequestBody LocationRequest request) {
        return locationService.changeById(id, request);
    }

}
