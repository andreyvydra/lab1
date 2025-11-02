package itmo.is.lab1.repositories;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends GeneralRepository<Product> {
    List<Product> findByNameStartingWithIgnoreCase(String prefix);
    List<Product> findByRatingLessThan(int rating);
    List<Product> findByManufacturer_Id(Long manufacturerId);
    List<Product> findByUnitOfMeasure(UnitOfMeasure unit);
    Product findFirstByOwner_Id(Long ownerId);
    void deleteByCoordinates_Id(Long coordinatesId);
    void deleteByOwner_Id(Long ownerId);
}
