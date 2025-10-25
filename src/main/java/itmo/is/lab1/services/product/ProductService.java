package itmo.is.lab1.services.product;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.product.requests.ProductRequest;
import itmo.is.lab1.services.product.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
