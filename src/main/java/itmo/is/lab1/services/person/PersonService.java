package itmo.is.lab1.services.person;

import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.person.requests.PersonRequest;
import itmo.is.lab1.services.person.responses.PersonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService extends GeneralService<PersonRequest, PersonResponse, Person> {
    @Autowired
    private LocationService locationService;

    @Override
    protected PersonResponse buildResponse(Person element) {
        PersonResponse personResponse = new PersonResponse();
        personResponse.setValues(element);
        return personResponse;
    }

    @Override
    protected Person buildEntity(PersonRequest request) {
        Person person = new Person();
        person.setValues(request, locationService);
        return person;
    }

    @Override
    public PersonResponse updateById(Long id, PersonRequest request) {
        Person entity = getEntityById(id);
        entity.setValues(request, entity.getUser());
        entity.setValues(request, locationService);
        repository.save(entity);
        return buildResponse(entity);
    }
}
