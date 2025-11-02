package itmo.is.lab1.services.product;

import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.models.product.Organization;
import itmo.is.lab1.repositories.AddressRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.common.errors.NotFoundException;
import itmo.is.lab1.services.product.requests.OrganizationRequest;
import itmo.is.lab1.services.product.responses.OrganizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrganizationService extends GeneralService<OrganizationRequest, OrganizationResponse, Organization, OrganizationRepository> {

    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    protected OrganizationResponse buildResponse(Organization element) {
        OrganizationResponse response = new OrganizationResponse();
        response.setValues(element);
        return response;
    }

    @Override
    protected Organization buildEntity(OrganizationRequest request) {
        Organization organization = new Organization();
        organization.setValues(request, userService.getCurrentUser(), addressRepository);
        return organization;
    }

    @Override
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
    public OrganizationResponse create(OrganizationRequest request) {
        if (repository.existsOrganizationByFullName(request.getFullName())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Организация с таким полным именем уже существует");
        }
        Organization entity = buildEntity(request);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Override
    public OrganizationResponse updateById(Long id, OrganizationRequest request) {
        Organization entity = getOwnedEntityById(id);
        if (repository.existsOrganizationByFullNameAndIdNot(request.getFullName(), id)) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Организация с таким полным именем уже существует");
        }
        entity.setValues(request, entity.getUser(), addressRepository);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Override
    @Transactional
    public itmo.is.lab1.services.common.responses.GeneralMessageResponse deleteById(Long id) {
        var products = productRepository.findByManufacturer_Id(id);
        for (var p : products) { p.setManufacturer(null); }
        productRepository.saveAll(products);
        return super.deleteById(id);
    }
}
