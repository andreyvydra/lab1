package itmo.is.lab1.models.location;


import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.location.requests.LocationRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "location")
public class Location extends GeneralEntity<LocationRequest> {

    @NotNull(message = "x не может быть null")
    @Column(nullable = false)
    private Integer x; //Поле не может быть null

    @NotNull(message = "y не может быть null")
    @Column(nullable = false)
    private Long y; //Поле не может быть null

    private long z;

    private String name; //Поле может быть null

    @OneToMany(mappedBy = "town", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();


    @Override
    public void setValues(LocationRequest request, User user) {
        super.setValues(request, user);
        this.setX(request.getX());
        this.setY(request.getY());
        this.setName(request.getName());
        this.setZ(request.getZ());
    }
}

