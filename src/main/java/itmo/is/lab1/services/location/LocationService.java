package itmo.is.lab1.services.location;

import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationResponse;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface LocationService {

    @NotNull
    List<LocationResponse> findAll();

    @NotNull
    LocationResponse findById(Long id);

    @NotNull
    LocationResponse deleteById(Long id);

    @NotNull
    LocationResponse createLocation(@NotNull LocationRequest request);

    @NotNull
    LocationResponse changeById(Long id, LocationRequest request);
}
