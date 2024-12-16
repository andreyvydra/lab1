package itmo.is.lab1.models.person;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.person.requests.PersonRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Optional;


@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "person")
public class Person extends GeneralEntity<PersonRequest> {
    @Column(nullable = false)
    private String name; //Поле не может быть null, Строка не может быть пустой
    @Enumerated(EnumType.STRING)
    private Color eyeColor; //Поле может быть null

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color hairColor; //Поле не может быть null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Location location; //Поле может быть null

    @Column(nullable = false, unique = true)
    private String passportID; //Значение этого поля должно быть уникальным, Поле не может быть null
    @Enumerated(EnumType.STRING)
    private Country nationality; //Поле может быть null


    public void setValues(PersonRequest request, LocationService service) {
        super.setValues(request, service.getUserService().getCurrentUser());
        this.eyeColor = request.getEyeColor();
        this.name = request.getName();
        this.hairColor = request.getHairColor();
        Optional<Location> location1 = service.getRepository().findById(request.getLocation());
        this.location = location1.orElse(null);
        this.passportID = request.getPassportID();
        this.nationality = request.getNationality();
    }
}
