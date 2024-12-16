package itmo.is.lab1.services.dragon;

import itmo.is.lab1.models.dragon.DragonCave;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.dragon.requests.DragonCaveRequest;
import itmo.is.lab1.services.dragon.responses.DragonCaveResponse;
import org.springframework.stereotype.Service;

@Service
public class DragonCaveService extends GeneralService<DragonCaveRequest, DragonCaveResponse, DragonCave> {
    @Override
    protected DragonCaveResponse buildResponse(DragonCave element) {
        DragonCaveResponse dragonCaveResponse = new DragonCaveResponse();
        dragonCaveResponse.setValues(element);
        return dragonCaveResponse;
    }

    @Override
    protected DragonCave buildEntity(DragonCaveRequest request) {
        DragonCave dragonCave = new DragonCave();
        dragonCave.setValues(request, userService.getCurrentUser());
        return dragonCave;
    }
}
