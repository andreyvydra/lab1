package itmo.is.lab1.services.person.requests;

import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    private Country nationality; //

    @NotNull(message = "height не должно быть null.")
    @Positive(message = "height должно быть больше 0")
    private Float height;

}
