package itmo.is.lab1.services.dragon;

import itmo.is.lab1.models.dragon.Dragon;
import itmo.is.lab1.models.user.enums.Role;
import itmo.is.lab1.repositories.*;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.dragon.requests.DragonRequest;
import itmo.is.lab1.services.dragon.responses.DragonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class

DragonService extends GeneralService<DragonRequest, DragonResponse, Dragon, DragonRepository> {
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
        Dragon entity = getOwnedEntityById(id);
        entity.setValues(request, entity.getUser(),
                pr, dr, dcr, cr);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Retryable(
            value = {
                    CannotAcquireLockException.class,
                    OptimisticLockingFailureException.class,
                    PessimisticLockingFailureException.class
            },
            maxAttempts = 5,
            backoff = @Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public GeneralMessageResponse deleteByAge(Integer age, Boolean all) {
        List<Dragon> dragonList = repository.findByAge(age);
        if (dragonList.isEmpty()) {
            return new GeneralMessageResponse().setMessage("Драконов такого возраста не было найдено!");
        }

        boolean deleted = false;
        for (Dragon dragon : dragonList) {
            if (dragon.getUser() == userService.getCurrentUser() || (dragon.getIsChangeable() &&
                    userService.getCurrentUser().getRole() == Role.ROLE_ADMIN)) {
                repository.delete(dragon);
                deleted = true;
                if (!all) {
                    break;
                }
            }
        }

        if (deleted) {
            messagingTemplate.convertAndSend("/topic/entities", "Было произведено удаление.");
            return new GeneralMessageResponse().setMessage("Было произведено удаление.");
        }
        return new GeneralMessageResponse().setMessage("Драконов такого возраста не было найдено!");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Boolean> getUniqueSpeaking() {
        return repository.findDistinctSpeakingValues();
    }
}
