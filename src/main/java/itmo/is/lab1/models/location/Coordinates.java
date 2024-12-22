package itmo.is.lab1.models.location;


import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.location.requests.CoordinatesRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "coordinates")
public class Coordinates extends GeneralEntity<CoordinatesRequest> {
    @NotNull(message = "x не может быть null")
    @Column(nullable = false)
    private Long x; //Поле не может быть null

    @NotNull(message = "y не может быть null")
    @Min(value = -348, message = "Значение поля y должно быть больше -349")
    @Column(nullable = false)
    private Integer y; //Значение поля должно быть больше -349

    @Override
    public void setValues(CoordinatesRequest request, User user) {
        super.setValues(request, user);
        x = request.getX();
        y = request.getY();
    }
}
