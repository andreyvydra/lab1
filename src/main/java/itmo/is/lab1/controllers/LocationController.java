package itmo.is.lab1.controllers;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.location.requests.LocationRequest;
import itmo.is.lab1.services.location.responses.LocationResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/location")
public class LocationController extends GeneralController<LocationRequest, LocationResponse, Location, LocationService> {
}
