package itmo.is.lab1.models.product;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.services.product.requests.AddressRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "address")
public class Address extends GeneralEntity<AddressRequest> {

    private String street;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "town_id", nullable = false, foreignKey = @ForeignKey(name = "fk_address_town_id"))
    private Location town;

    public void setValues(AddressRequest request, User user, LocationRepository locationRepository) {
        super.setValues(request, user);
        street = request.getStreet();
        if (request.getTownId() != null) {
            Optional<Location> location = locationRepository.findById(request.getTownId());
            location.ifPresent(value -> town = value);
        }
    }
}



