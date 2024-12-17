package itmo.is.lab1.controllers;

import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.services.location.CoordinatesService;
import itmo.is.lab1.services.location.requests.CoordinatesRequest;
import itmo.is.lab1.services.location.responses.CoordinatesResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/coordinates")
public class CoordinatesController extends GeneralController<CoordinatesRequest, CoordinatesResponse, Coordinates, CoordinatesService> {
}
