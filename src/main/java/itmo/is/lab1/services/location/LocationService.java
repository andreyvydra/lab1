package itmo.is.lab1.services.location;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationGetResponse;
import itmo.is.lab1.services.location.responses.LocationResponse;
import org.springframework.stereotype.Service;


@Service
public class LocationService extends GeneralService<LocationRequest, LocationResponse, Location, LocationRepository> {

    protected LocationGetResponse buildResponse(Location location) {
        LocationGetResponse locationGetResponse = new LocationGetResponse();
        locationGetResponse.setValues(location);
        return locationGetResponse;
    }

    protected Location buildEntity(LocationRequest locationReq) {
        Location location = new Location();
        location.setValues(locationReq, userService.getCurrentUser());
        return location;
    }

}
