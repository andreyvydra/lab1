package itmo.is.lab1.services.location;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.services.location.errors.LocationBadRequestException;
import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationGetResponse;
import itmo.is.lab1.services.location.responses.LocationInfoResponse;
import itmo.is.lab1.services.location.responses.LocationResponse;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.services.location.errors.LocationForbiddenException;
import itmo.is.lab1.services.location.errors.LocationNotFoundException;
import itmo.is.lab1.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private UserService userService;

    @Override
    public List<LocationResponse> findAll() {
        return locationRepository.findAll()
                .stream().map(this::buildLocationResponse)
                .collect(Collectors.toList());
    }

    @Override
    public LocationResponse findById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return buildLocationResponse(location.get());
        }
        throw new LocationNotFoundException();
    }

    @Override
    public LocationResponse deleteById(Long id) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        Location location = null;
        if (locationOpt.isPresent()) {
            location = locationOpt.get();
        }
        if (location == null) {
            throw new LocationNotFoundException();
        }

        if ((location.getIsChangeable() && userService.getCurrentUser().getRole() == Role.ROLE_ADMIN)
                || location.getUser() == userService.getCurrentUser()) {
            locationRepository.delete(location);
            return new LocationInfoResponse()
                    .setId(id)
                    .setMessage("Location был удалён.");
        }

        throw new LocationForbiddenException();
    }


    @Override
    public LocationGetResponse createLocation(LocationRequest request) {
        if (!request.isValid()) {
            throw new LocationBadRequestException();
        }

        Location location = buildLocationRequest(request);
        locationRepository.save(location);
        return buildLocationResponse(location);
    }

    @Override
    public LocationResponse changeById(Long id, LocationRequest request) {
        Optional<Location> locationOpt = locationRepository.findById(id);
        Location location = null;
        if (locationOpt.isPresent()) {
            location = locationOpt.get();
        }

        if (location == null) {
            throw new LocationNotFoundException();
        }

        if (!(location.getIsChangeable() && userService.getCurrentUser().getRole() == Role.ROLE_ADMIN)
                && location.getUser() != userService.getCurrentUser()) {
            throw new LocationForbiddenException();
        }

        if (!request.isValid()) {
            throw new LocationBadRequestException();
        }

        location.setName(request.getName());
        location.setX(request.getX());
        location.setY(request.getY());
        location.setIsChangeable(request.getIsChangeable());
        locationRepository.save(location);

        return buildLocationResponse(location);
    }

    private LocationGetResponse buildLocationResponse(Location location) {
        return new LocationGetResponse()
                .setId(location.getId())
                .setX(location.getX())
                .setY(location.getY())
                .setName(location.getName())
                .setIsChangeable(location.getIsChangeable())
                .setUser(location.getUser().getId());
    }

    private Location buildLocationRequest(LocationRequest location) {
        return new Location()
                .setX(location.getX())
                .setY(location.getY())
                .setName(location.getName())
                .setIsChangeable(location.getIsChangeable())
                .setUser(userService.getCurrentUser());
    }
}
