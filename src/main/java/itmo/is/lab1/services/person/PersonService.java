package itmo.is.lab1.services.person;

import itmo.is.lab1.models.ImportHistory;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.services.common.GeneralService;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.import_history.ImportHistoryService;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.person.requests.PersonImportRequest;
import itmo.is.lab1.services.person.requests.PersonRequest;
import itmo.is.lab1.services.person.responses.PersonResponse;
import jakarta.validation.ValidationException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersonService extends GeneralService<PersonRequest, PersonResponse, Person, PersonRepository> {
    @Autowired
    private LocationService locationService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private ImportHistoryService importHistoryService;

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
        Person entity = getOwnedEntityById(id);
        entity.setValues(request, entity.getUser());
        entity.setValues(request, locationService);
        repository.save(entity);
        messagingTemplate.convertAndSend("/topic/entities", buildResponse(entity));
        return buildResponse(entity);
    }

    @Transactional
    public void importFromXlsx(MultipartFile file) throws IOException, ValidationException {
        List<Person> persons = new ArrayList<>();
        ImportHistory history = importHistoryService.startImport(userService.getCurrentUser());

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                PersonImportRequest request = parseRowToPersonRequest(row);
                validatePersonRequest(request);
                Person person = buildPersonFromRequest(request);
                persons.add(person);
            }

            locationRepository.saveAll(persons.stream().map(Person::getLocation).collect(Collectors.toList()));
            repository.saveAll(persons);
            importHistoryService.finishImport(history, "SUCCESS", persons.size());
            messagingTemplate.convertAndSend("/topic/entities", "Imported from XLSX");
        } catch (Exception e) {
            importHistoryService.finishImport(history, "FAILED", 0);
            throw e;
        }
    }

    private PersonImportRequest parseRowToPersonRequest(Row row) {
        PersonImportRequest request = new PersonImportRequest();
        request.setName(row.getCell(0).getStringCellValue());
        request.setEyeColor(Color.valueOf(row.getCell(1).getStringCellValue()));
        request.setHairColor(Color.valueOf(row.getCell(2).getStringCellValue()));
        request.setPassportID(row.getCell(3).getStringCellValue());
        request.setNationality(Country.valueOf(row.getCell(4).getStringCellValue()));
        request.setIsChangeable(Boolean.valueOf(row.getCell(5).getStringCellValue()));

        request.setX((int) row.getCell(6).getNumericCellValue());
        request.setY((long) row.getCell(7).getNumericCellValue());
        request.setNameLoc(row.getCell(8).getStringCellValue());

        return request;
    }

    private void validatePersonRequest(@Validated PersonImportRequest request) throws ValidationException {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'name' не может быть пустым.");
        }
        if (request.getHairColor() == null) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'hairColor' не может быть пустым.");
        }
        if (request.getPassportID() == null || request.getPassportID().isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Поле 'passportID' не может быть пустым.");
        }
        while (repository.existsPersonByPassportID(request.getPassportID())) {
            request.setPassportID(UUID.randomUUID().toString());
        }
    }

    private Person buildPersonFromRequest(PersonImportRequest request) {
        Person person = new Person();
        person.setValues(request, locationService);
        person.setUser(userService.getCurrentUser());

        Location location = new Location();
        location.setX(request.getX());
        location.setY(request.getY());
        location.setName(request.getName());
        location.setIsChangeable(request.getIsChangeable());
        location.setUser(userService.getCurrentUser());

        person.setLocation(location);

        return person;
    }
}
