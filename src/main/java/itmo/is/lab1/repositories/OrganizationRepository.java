package itmo.is.lab1.repositories;

import itmo.is.lab1.models.product.Organization;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends GeneralRepository<Organization> {
    Boolean existsOrganizationByFullName(String fullName);
    Boolean existsOrganizationByFullNameAndIdNot(String fullName, Long id);
    java.util.List<Organization> findByOfficialAddress_Id(Long addressId);
}
