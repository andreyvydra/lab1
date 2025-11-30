package itmo.is.lab1.models.location;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.location.requests.LocationRequest;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "location")
public class Location extends GeneralEntity<LocationRequest> {

    @NotNull(message = "x must not be null")
    @Column(nullable = false)
    private Integer x;

    @NotNull(message = "y must not be null")
    @Column(nullable = false)
    private Long y;

    private long z;

    private String name;

    @Override
    public void setValues(LocationRequest request, User user) {
        super.setValues(request, user);
        this.setX(request.getX());
        this.setY(request.getY());
        this.setName(request.getName());
        this.setZ(request.getZ());
    }

}
