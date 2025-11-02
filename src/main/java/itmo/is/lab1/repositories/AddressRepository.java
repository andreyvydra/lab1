package itmo.is.lab1.repositories;

import itmo.is.lab1.models.product.Address;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends GeneralRepository<Address> {
    List<Address> findByTown_Id(Long townId);
    void deleteByTown_Id(Long townId);
}
