package itmo.is.lab1.services.dragon.responses;

import itmo.is.lab1.models.dragon.Dragon;
import itmo.is.lab1.models.dragon.DragonCharacter;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonResponse extends GeneralEntityResponse {
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

    @NotNull(message = "name не может быть null")
    private DragonCharacter character; //Поле не может быть null

    private Long head;

    public void setValues(Dragon entity) {
        super.setValues(entity);
        name = entity.getName();
        coordinates = entity.getCoordinates().getId();
        creationDate = entity.getCreationDate().toString();
        cave = entity.getCave().getId();
        if (entity.getKiller() != null) killer = entity.getKiller().getId();
        age = entity.getAge();
        speaking = entity.getSpeaking();
        color = entity.getColor();
        character = entity.getCharacter();
        if (entity.getHead() != null) head = entity.getHead().getId();
    }
}
