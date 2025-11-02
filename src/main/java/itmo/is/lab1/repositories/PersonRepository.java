package itmo.is.lab1.repositories;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GeneralRepository<Person>{
    Boolean existsPersonByPassportID(String personId);
    Boolean existsPersonByPassportIDAndIdNot(String personId, Long id);
    java.util.List<Person> findByLocation_Id(Long locationId);
    void deletePersonByLocation_Id(Long locationId);
}
