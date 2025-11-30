package itmo.is.lab1.models.person;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.person.enums.Color;
import itmo.is.lab1.models.person.enums.Country;
import itmo.is.lab1.services.location.LocationService;
import itmo.is.lab1.services.person.requests.PersonRequest;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "person")
public class Person extends GeneralEntity<PersonRequest> {

    @NotNull(message = "Поле 'name' не должно быть null.")
    @NotBlank(message = "Поле 'name' не должно быть пустым.")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @NotNull(message = "Поле 'hairColor' не должно быть null.")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id", nullable = true, foreignKey = @ForeignKey(name = "fk_person_location_id"))
    private Location location;

    @NotNull(message = "Поле 'passportID' не должно быть null.")
    @Column(nullable = false, unique = true)
    private String passportID;

    private float height;

    @Enumerated(EnumType.STRING)
    private Country nationality;

    public void setValues(PersonRequest request, LocationService service) {
        super.setValues(request, service.getUserService().getCurrentUser());
        this.eyeColor = request.getEyeColor();
        this.name = request.getName();
        this.hairColor = request.getHairColor();
        if (request.getLocation() != null) {
            Optional<Location> location1 = service.getRepository().findById(request.getLocation());
            this.location = location1.orElse(null);
        }
        this.passportID = request.getPassportID();
        this.nationality = request.getNationality();
        this.height = request.getHeight();
    }
}
