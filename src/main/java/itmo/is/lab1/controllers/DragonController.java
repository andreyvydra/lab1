package itmo.is.lab1.controllers;

import itmo.is.lab1.models.dragon.Dragon;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.dragon.DragonService;
import itmo.is.lab1.services.dragon.requests.DragonRequest;
import itmo.is.lab1.services.dragon.responses.DragonResponse;
import itmo.is.lab1.specification.PaginatedResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dragon")
public class DragonController extends GeneralController<DragonRequest, DragonResponse, Dragon, DragonService> {
    @DeleteMapping(value = "/deleteAllByAge")
    public @NotNull GeneralMessageResponse deleteAllByAge(
            @RequestParam(defaultValue = "1") Integer age
    ) {
        return service.deleteByAge(age, true);
    }

    @DeleteMapping(value = "/deleteByAge")
    public @NotNull GeneralMessageResponse deleteByAge(
            @RequestParam(defaultValue = "1") Integer age
    ) {
        return service.deleteByAge(age, false);
    }

    @GetMapping(value = "/speaking")
    public @NotNull List<Boolean> getUniqueSpeaking() {
        return service.getUniqueSpeaking();
    }
}
