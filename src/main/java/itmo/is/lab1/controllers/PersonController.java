package itmo.is.lab1.controllers;

import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.common.responses.GeneralMessageResponse;
import itmo.is.lab1.services.person.PersonService;
import itmo.is.lab1.services.person.requests.PersonRequest;
import itmo.is.lab1.services.person.responses.PersonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping(value = "/api/person")
public class PersonController extends GeneralController<PersonRequest, PersonResponse, Person, PersonService> {

    @PostMapping("/import")
    public GeneralMessageResponse importPersons(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
                throw new GeneralException(HttpStatus.BAD_REQUEST, "Поддерживается только импорт CSV-файлов.");
            }
            int added = service.importFromCsv(file);
            return new GeneralMessageResponse()
                    .setMessage("Импорт завершён. Добавлено записей: " + added + ".");
        } catch (IOException e) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "Ошибка чтения CSV-файла, проверьте формат.");
        }
    }
}

