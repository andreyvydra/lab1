package itmo.is.lab1.services.dragon;

import itmo.is.lab1.models.dragon.Dragon;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.DragonCaveRepository;
import itmo.is.lab1.repositories.DragonHeadRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.dragon.requests.DragonRequest;
import itmo.is.lab1.services.dragon.responses.DragonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DragonService extends GeneralService<DragonRequest, DragonResponse, Dragon> {
    @Autowired
    private PersonRepository pr;
    @Autowired
    private DragonHeadRepository dr;
    @Autowired
    private DragonCaveRepository dcr;
    @Autowired
    private CoordinatesRepository cr;

    @Override
    protected DragonResponse buildResponse(Dragon element) {
        DragonResponse dragonResponse = new DragonResponse();
        dragonResponse.setValues(element);
        return dragonResponse;
    }

    @Override
    protected Dragon buildEntity(DragonRequest request) {
        Dragon dragon = new Dragon();
        dragon.setValues(request, userService.getCurrentUser());
        dragon.setValues(request, userService.getCurrentUser(), pr, dr, dcr, cr);
        return dragon;
    }

    @Override
    public DragonResponse updateById(Long id, DragonRequest request) {
        System.out.println("UPDATE");
        Dragon entity = getEntityById(id);
        entity.setValues(request, entity.getUser(),
                pr, dr, dcr, cr);
        repository.save(entity);
        return buildResponse(entity);
    }
}
