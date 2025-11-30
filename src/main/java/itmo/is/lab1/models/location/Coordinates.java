package itmo.is.lab1.models.location;


import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.location.requests.CoordinatesRequest;
import jakarta.persistence.Cacheable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "coordinates")
public class Coordinates extends GeneralEntity<CoordinatesRequest> {
    @NotNull(message = "x не может быть null")
    @Max(value = 468, message = "Максимальное значение x = 468")
    @Column(nullable = false)
    private Integer x; //Максимальное значение поля: 468, Поле не может быть null

    @Min(value = -452, message = "Значение поля y должно быть больше -452")
    private double y; //Значение поля должно быть больше -452

    @Override
    public void setValues(CoordinatesRequest request, User user) {
        super.setValues(request, user);
        x = request.getX();
        y = request.getY();
    }
}



