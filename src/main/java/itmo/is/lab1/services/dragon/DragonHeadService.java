package itmo.is.lab1.services.dragon;

import itmo.is.lab1.models.dragon.DragonHead;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.dragon.requests.DragonHeadRequest;
import itmo.is.lab1.services.dragon.responses.DragonHeadResponse;
import org.springframework.stereotype.Service;

@Service
public class DragonHeadService extends GeneralService<DragonHeadRequest, DragonHeadResponse, DragonHead> {
    @Override
    protected DragonHeadResponse buildResponse(DragonHead element) {
        DragonHeadResponse dragonHeadResponse = new DragonHeadResponse();
        dragonHeadResponse.setValues(element);
        return dragonHeadResponse;
    }

    @Override
    protected DragonHead buildEntity(DragonHeadRequest request) {
        DragonHead dragonHead = new DragonHead();
        dragonHead.setValues(request, userService.getCurrentUser());
        return dragonHead;
    }
}
