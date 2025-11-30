package itmo.is.lab1.services.person;

import itmo.is.lab1.models.ImportHistory;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.repositories.ProductRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.import_history.ImportHistoryService;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.person.requests.PersonImportRequest;
import itmo.is.lab1.services.person.requests.PersonRequest;
import itmo.is.lab1.services.person.responses.PersonResponse;
import itmo.is.lab1.services.storage.FileStorageService;
import itmo.is.lab1.services.storage.FileStorageService.StoredObject;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PersonService extends GeneralService<PersonRequest, PersonResponse, Person, PersonRepository> {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ImportHistoryService importHistoryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    @Lazy
    private PersonService self;

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
    @Retryable(
            retryFor = SQLException.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PersonResponse create(PersonRequest request) {
        validateNameAndLocationUnique(request.getName(), request.getLocation(), null);
        if (repository.existsPersonByPassportID(request.getPassportID())) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Человек с таким паспортом уже существует.");
        }
        Person entity = buildEntity(request);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Override
    @Retryable(
            retryFor = {
                    SQLException.class,
            },
            maxAttempts = 10,
            backoff = @Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public PersonResponse updateById(Long id, PersonRequest request) {
        Person entity = getOwnedEntityById(id);
        if (repository.existsPersonByPassportIDAndIdNot(request.getPassportID(), id)) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Человек с таким паспортом уже существует.");
        }
        validateNameAndLocationUnique(request.getName(), request.getLocation(), id);
        entity.setValues(request, locationService);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    public int importFromCsv(MultipartFile file) throws IOException, ValidationException {
        StoredObject storedObject = fileStorageService.storeImportFile(file);
        ImportHistory history = importHistoryService.startImport(userService.getCurrentUser());
        try {
            int added = self.importFromCsvInternal(file);
            importHistoryService.finishImport(history, "SUCCESS", added,
                    storedObject.getOriginalFileName(), storedObject.getObjectKey());
            messagingTemplate.convertAndSend("/topic/entities", "Imported from CSV");
            log.info("Success id: {}, stat: {}", history.getId(), history.getStatus());
            return added;
        } catch (Throwable e) {
            importHistoryService.finishImport(history, "FAILED", 0, null, null);
            log.info("id: {}, stat: {}", history.getId(), history.getStatus());
            log.info("Error importing from CSV {}", e.getMessage());
            throw e;
        }
    }

    @Retryable(
            retryFor = {SQLException.class},
            maxAttempts = 10,
            backoff = @Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int importFromCsvInternal(MultipartFile file) throws IOException, ValidationException {
        List<Person> persons = new ArrayList<>();

        Set<String> passportsInFile = new HashSet<>();
        Set<String> compositeInFile = new HashSet<>();
        int added = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim()
                     .parse(reader)) {

            for (CSVRecord record : parser) {
                PersonImportRequest request = parseCsvToPersonRequest(record);
                validatePersonRequest(request);

                if (!passportsInFile.add(request.getPassportID())) {
                    throw new GeneralException(
                            HttpStatus.BAD_REQUEST,
                            "Паспорт " + request.getPassportID() + " встречается в файле более одного раза."
                    );
                }

                String compositeKey = request.getName() + "|" + request.getX() + "|" + request.getY() + "|" + request.getNameLoc();
                if (!compositeInFile.add(compositeKey)) {
                    throw new GeneralException(
                            HttpStatus.BAD_REQUEST,
                            "Комбинация полей (name, x, y, nameLoc) для (" +
                                    request.getName() + ", " + request.getX() + ", " +
                                    request.getY() + ", " + request.getNameLoc() +
                                    ") встречается в файле более одного раза."
                    );
                }

                if (repository.existsPersonByPassportID(request.getPassportID())) {
                    throw new GeneralException(
                            HttpStatus.BAD_REQUEST,
                            "Человек с паспортом " + request.getPassportID() + " уже существует."
                    );
                }

                Specification<Person> compositeSpec = (root, query, cb) -> {
                    var loc = root.join("location");
                    return cb.and(
                            cb.equal(root.get("name"), request.getName()),
                            cb.equal(loc.get("x"), request.getX()),
                            cb.equal(loc.get("y"), request.getY()),
                            cb.equal(loc.get("name"), request.getNameLoc())
                    );
                };
                if (repository.count(compositeSpec) > 0) {
                    throw new GeneralException(
                            HttpStatus.BAD_REQUEST,
                            "Персона с комбинацией полей (name, x, y, nameLoc) уже существует в системе."
                    );
                }

                Person person = buildPersonFromRequest(request);
                persons.add(person);
                added++;
            }

            if (added == 0) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "Файл указан пустой или не содержит ни одного ряда.");
            }

            locationRepository.saveAll(persons.stream().map(Person::getLocation).toList());
            repository.saveAll(persons);
            return added;
        }
    }

    private PersonImportRequest parseCsvToPersonRequest(CSVRecord record) {
        PersonImportRequest request = new PersonImportRequest();
        request.setName(record.get("name"));
        request.setEyeColor(Color.valueOf(record.get("eyeColor")));
        request.setHairColor(Color.valueOf(record.get("hairColor")));
        request.setPassportID(record.get("passportID"));
        request.setNationality(Country.valueOf(record.get("nationality")));
        request.setIsChangeable(Boolean.valueOf(record.get("isChangeable")));
        request.setHeight(Float.valueOf(record.get("height")));
        request.setX(Integer.valueOf(record.get("x")));
        request.setY(Long.valueOf(record.get("y")));
        request.setNameLoc(record.get("nameLoc"));
        return request;
    }

    private void validatePersonRequest(@Validated PersonImportRequest request) throws ValidationException {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'name' не должно быть пустым.");
        }
        if (request.getHairColor() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'hairColor' не должно быть пустым.");
        }
        if (request.getPassportID() == null || request.getPassportID().isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'passportID' не должно быть пустым.");
        }
        if (request.getHeight() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'height' не должно быть пустым.");
        }
    }

    private Person buildPersonFromRequest(PersonImportRequest request) {
        Person person = new Person();
        person.setValues(request, locationService);
        person.setUser(userService.getCurrentUser());

        Location location = new Location();
        location.setX(request.getX());
        location.setY(request.getY());
        location.setName(request.getNameLoc());
        location.setIsChangeable(request.getIsChangeable());
        location.setUser(userService.getCurrentUser());

        person.setLocation(location);

        return person;
    }

    private void validateNameAndLocationUnique(String name, Long locationId, Long currentPersonId) {
        if (name == null || locationId == null) {
            return;
        }
        boolean exists;
        if (currentPersonId == null) {
            exists = Boolean.TRUE.equals(repository.existsByNameAndLocation_Id(name, locationId));
        } else {
            exists = Boolean.TRUE.equals(repository.existsByNameAndLocation_IdAndIdNot(name, locationId, currentPersonId));
        }
        if (exists) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Комбинация полей 'name' и 'location' должна быть уникальной.");
        }
    }

    @Override
    @Retryable(
            retryFor = SQLException.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 500, maxDelay = 5000, multiplier = 2.0, random = true)
    )
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public itmo.is.lab1.services.common.responses.GeneralMessageResponse deleteById(Long id) {
        productRepository.deleteByOwner_Id(id);
        return super.deleteById(id);
    }
}
