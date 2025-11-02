package itmo.is.lab1.models.product;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.repositories.LocationRepository;
import itmo.is.lab1.services.product.requests.AddressRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
    @JoinColumn(name = "town_id", nullable = false)
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

