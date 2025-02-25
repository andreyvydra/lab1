package itmo.is.lab1.repositories;

import itmo.is.lab1.models.person.Person;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends GeneralRepository<Person>{
    Boolean existsPersonByPassportID(String personId);
}
