package itmo.is.lab1.controllers;

import itmo.is.lab1.models.dragon.Dragon;
import itmo.is.lab1.services.dragon.DragonService;
import itmo.is.lab1.services.dragon.requests.DragonRequest;
import itmo.is.lab1.services.dragon.responses.DragonResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/dragon")
public class DragonController extends GeneralController<DragonRequest, DragonResponse, Dragon, DragonService> {
}
