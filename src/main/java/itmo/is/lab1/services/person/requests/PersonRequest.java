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

    @NotNull(message = "Поле 'name' не должно быть null.")
    @NotBlank(message = "Поле 'name' не должно быть пустым.")
    private String name;

    private Color eyeColor;

    @NotNull(message = "Поле 'hairColor' не должно быть null.")
    private Color hairColor;

    private Long location;

    @NotNull(message = "Поле 'passportID' не должно быть null.")
    private String passportID;

    private Country nationality;

    @NotNull(message = "Поле 'height' не должно быть null.")
    @Positive(message = "Поле 'height' должно быть больше 0.")
    private Float height;
}

