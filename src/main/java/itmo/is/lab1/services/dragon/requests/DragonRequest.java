package itmo.is.lab1.services.dragon.requests;

import itmo.is.lab1.models.dragon.DragonCave;
import itmo.is.lab1.models.dragon.DragonCharacter;
import itmo.is.lab1.models.dragon.DragonHead;
import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonRequest extends GeneralEntityRequest {
    @NotNull(message = "name не может быть null")
    @NotBlank(message = "name не может быть пустой")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @NotNull(message = "coordinates не может быть null")
    private Long coordinates; //Поле не может быть null

    @NotNull(message = "creationDate не может быть null")
    private String creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @NotNull(message = "cave не может быть null")
    private Long cave; //Поле не может быть null

    private Long killer; //Поле может быть null

    @Min(value = 1, message = "Значение поля age должно быть больше 0")
    private Integer age; //Значение поля должно быть больше 0, Поле может быть null

    private Boolean speaking; //Поле может быть null
    private Color color; //Поле может быть null

    @NotNull(message = "character не может быть null")
    private DragonCharacter character; //Поле не может быть null

    private Long head;


}
