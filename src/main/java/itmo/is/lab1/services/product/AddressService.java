package itmo.is.lab1.services.product;

import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.AddressRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.product.requests.AddressRequest;
import itmo.is.lab1.services.product.responses.AddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends GeneralService<AddressRequest, AddressResponse, Address, AddressRepository> {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    protected AddressResponse buildResponse(Address element) {
        AddressResponse response = new AddressResponse();
        response.setValues(element);
        return response;
    }

    @Override
    protected Address buildEntity(AddressRequest request) {
        Address address = new Address();
        address.setValues(request, userService.getCurrentUser(), locationRepository);
        return address;
    }

    @Override
    @org.springframework.retry.annotation.Retryable(
            value = {
                    org.springframework.dao.CannotAcquireLockException.class,
                    org.springframework.dao.OptimisticLockingFailureException.class,
                    org.springframework.dao.PessimisticLockingFailureException.class
            },
            maxAttempts = 5,
            backoff = @org.springframework.retry.annotation.Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @org.springframework.transaction.annotation.Transactional(isolation = org.springframework.transaction.annotation.Isolation.SERIALIZABLE)
    public AddressResponse updateById(Long id, AddressRequest request) {
        Address entity = getOwnedEntityById(id);
        entity.setValues(request, entity.getUser(), locationRepository);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public itmo.is.lab1.services.common.responses.GeneralMessageResponse deleteById(Long id) {
        var orgs = organizationRepository.findByOfficialAddress_Id(id);
        for (var o : orgs) { o.setOfficialAddress(null); }
        organizationRepository.saveAll(orgs);
        return super.deleteById(id);
    }
}
