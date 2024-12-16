package itmo.is.lab1.services.person.requests;

import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PersonRequest extends GeneralEntityRequest {

    @NotNull(message = "name не может быть null.")
    @NotBlank(message = "name не может быть пустой.")
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Color eyeColor; //Поле может быть null

    @NotNull(message = "hairColor не может быть null.")
    private Color hairColor; //Поле не может быть null


    private Long location; //Поле может быть null

    @NotNull(message = "passportId не может быть null.")
    private String passportID; //Значение этого поля должно быть уникальным, Поле не может быть null

    private Country nationality; //Поле может быть null

    @Override
    public Boolean isValid() {
        return name != null && hairColor != null && passportID != null;
    }
}
