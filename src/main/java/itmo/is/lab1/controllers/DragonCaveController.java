package itmo.is.lab1.controllers;

import itmo.is.lab1.models.dragon.DragonCave;
import itmo.is.lab1.services.dragon.DragonCaveService;
import itmo.is.lab1.services.dragon.requests.DragonCaveRequest;
import itmo.is.lab1.services.dragon.responses.DragonCaveResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/dragonCave")
public class DragonCaveController extends GeneralController<DragonCaveRequest, DragonCaveResponse, DragonCave, DragonCaveService> {
}
