package itmo.is.lab1.controllers;

import itmo.is.lab1.models.dragon.DragonHead;
import itmo.is.lab1.services.dragon.DragonHeadService;
import itmo.is.lab1.services.dragon.requests.DragonHeadRequest;
import itmo.is.lab1.services.dragon.responses.DragonHeadResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/dragonHead")
public class DragonHeadController extends GeneralController<DragonHeadRequest, DragonHeadResponse, DragonHead, DragonHeadService> {
}
