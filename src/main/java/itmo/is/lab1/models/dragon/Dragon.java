package itmo.is.lab1.models.dragon;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.DragonCaveRepository;
import itmo.is.lab1.repositories.DragonHeadRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.services.common.errors.GeneralException;
import itmo.is.lab1.services.dragon.requests.DragonRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "dragon")
public class Dragon extends GeneralEntity<DragonRequest> {
    @NotBlank
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coordinate_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Coordinates coordinates; //Поле не может быть null

    @Column(nullable = false)
    private java.util.Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dragon_cave_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DragonCave cave; //Поле не может быть null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "killer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Person killer; //Поле может быть null

    @Min(value = 1, message = "Значение поля age должно быть больше 0")
    private Integer age; //Значение поля должно быть больше 0, Поле может быть null
    private Boolean speaking; //Поле может быть null
    @Enumerated(EnumType.STRING)
    private Color color; //Поле может быть null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DragonCharacter character; //Поле не может быть null

    @ManyToOne(fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "head_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DragonHead head;

    public void setValues(DragonRequest request, User user, PersonRepository pr, DragonHeadRepository dr, DragonCaveRepository dcr, CoordinatesRepository cr) {
        super.setValues(request, user);
        name = request.getName();
        Optional<Coordinates> coordinates1 = cr.findById(request.getCoordinates());
        if (coordinates1.isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "У дракона должна быть координата!");
        }
        coordinates = coordinates1.get();
        creationDate = new java.util.Date();
        Optional<DragonCave> dragonCave = dcr.findById(request.getCave());
        if (dragonCave.isEmpty()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, "У дракона должна быть пещера!");
        }
        cave = dragonCave.get();

        if (request.getKiller() != null) {
            Optional<Person> person = pr.findById(request.getKiller());
            person.ifPresent(value -> killer = value);
        } else {
            killer = null;
        }

        age = request.getAge();
        speaking = request.getSpeaking();
        color = request.getColor();

        character = request.getCharacter();

        if (request.getHead() != null) {
            Optional<DragonHead> dragonHead = dr.findById(request.getHead());
            dragonHead.ifPresent(value -> head = value);
        } else {
            head = null;
        }

    }
}
