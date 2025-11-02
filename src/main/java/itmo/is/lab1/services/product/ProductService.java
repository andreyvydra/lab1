package itmo.is.lab1.services.product;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.product.requests.ProductRequest;
import itmo.is.lab1.services.product.responses.ProductResponse;
import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import itmo.is.lab1.services.common.errors.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService extends GeneralService<ProductRequest, ProductResponse, Product, ProductRepository> {

    @Autowired
    private CoordinatesRepository coordinatesRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    protected ProductResponse buildResponse(Product element) {
        ProductResponse response = new ProductResponse();
        response.setValues(element);
        return response;
    }

    @Override
    protected Product buildEntity(ProductRequest request) {
        Product product = new Product();
        product.setValues(request, userService.getCurrentUser(),
                coordinatesRepository, organizationRepository, personRepository);
        return product;
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
    public ProductResponse updateById(Long id, ProductRequest request) {
        Product entity = getOwnedEntityById(id);
        entity.setValues(request, entity.getUser(),
                coordinatesRepository, organizationRepository, personRepository);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Transactional
    public ProductResponse deleteOneByOwner(Long ownerId) {
        Product product = repository.findFirstByOwner_Id(ownerId);
        if (product == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Нет продукта с таким владельцем");
        }
        repository.delete(product);
        messagingTemplate.convertAndSend("/topic/entities", product.getId());
        return buildResponse(product);
    }

    public List<ProductResponse> findByNameStartsWith(String prefix) {
        return repository.findByNameStartingWithIgnoreCase(prefix)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> findByRatingLessThan(int max) {
        return repository.findByRatingLessThan(max)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> findByManufacturer(Long manufacturerId) {
        return repository.findByManufacturer_Id(manufacturerId)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }

    public List<ProductResponse> findByUnit(String unit) {
        UnitOfMeasure u = null;
        try { if (unit != null && !unit.isEmpty()) u = UnitOfMeasure.valueOf(unit); } catch (Exception ignored) {}
        if (u == null) return List.of();
        return repository.findByUnitOfMeasure(u)
                .stream().map(this::buildResponse).collect(Collectors.toList());
    }
}
