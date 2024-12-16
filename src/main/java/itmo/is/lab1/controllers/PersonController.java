package itmo.is.lab1.controllers;

import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.services.person.PersonService;
import itmo.is.lab1.services.person.requests.PersonRequest;
import itmo.is.lab1.services.person.responses.PersonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/person")
public class PersonController extends GeneralController<PersonRequest, PersonResponse, Person, PersonService> {
}
