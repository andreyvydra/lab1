package itmo.is.lab1.services.person.responses;

import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PersonResponse extends GeneralEntityResponse {
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

    public void setValues(Person entity) {
        super.setValues(entity);
        name = entity.getName();
        eyeColor = entity.getEyeColor();
        hairColor = entity.getHairColor();
        if (entity.getLocation() != null) location = entity.getLocation().getId();
        passportID = entity.getPassportID();
        nationality = entity.getNationality();
    }
}
