package itmo.is.lab1.repositories;

import itmo.is.lab1.models.product.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GeneralRepository<Product> {
}

