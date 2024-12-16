package itmo.is.lab1.models.location;


import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.location.requests.LocationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "location")
public class Location extends GeneralEntity<LocationRequest> {

    @NotNull(message = "x не может быть null.")
    private Integer x; //Поле не может быть null

    @NotNull(message = "y не может быть null.")
    private Long y; //Поле не может быть null

    @NotBlank(message = "name не может быть пустым.")
    private String name; //Строка не может быть пустой, Поле может быть null


    @Override
    public void setValues(LocationRequest request, User user) {
        super.setValues(request, user);
        this.setX(request.getX());
        this.setY(request.getY());
        this.setName(request.getName());
    }
}
