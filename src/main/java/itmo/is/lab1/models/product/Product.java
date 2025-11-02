package itmo.is.lab1.models.product;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Coordinates;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.repositories.CoordinatesRepository;
import itmo.is.lab1.repositories.OrganizationRepository;
import itmo.is.lab1.repositories.PersonRepository;
import itmo.is.lab1.services.product.requests.ProductRequest;
import itmo.is.lab1.models.person.Person;
import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "product")
public class Product extends GeneralEntity<ProductRequest> {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coordinates_id", nullable = false)
    private Coordinates coordinates;

    @Column(nullable = false)
    private Date creationDate;

    private UnitOfMeasure unitOfMeasure;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manufacturer_id")
    private Organization manufacturer;

    private Long price;

    private Integer manufactureCost;

    private int rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private Person owner;

    public void setValues(
            ProductRequest request,
            User user,
            CoordinatesRepository coordinatesRepository,
            OrganizationRepository organizationRepository,
            PersonRepository personRepository
    ) {
        super.setValues(request, user);
        this.name = request.getName();
        this.rating = request.getRating();
        this.unitOfMeasure = request.getUnitOfMeasure();
        this.price = request.getPrice();
        this.manufactureCost = request.getManufactureCost();
        if (request.getCoordinates() != null) {
            Optional<Coordinates> coordinates1 = coordinatesRepository.findById(request.getCoordinates());
            coordinates1.ifPresent(value -> this.coordinates = value);
        }
        if (request.getManufacturer() != null) {
            Optional<Organization> organization = organizationRepository.findById(request.getManufacturer());
            organization.ifPresent(value -> this.manufacturer = value);
        }
        if (request.getOwner() != null) {
            Optional<Person> person = personRepository.findById(request.getOwner());
            person.ifPresent(value -> this.owner = value);
        }
    }

    @PrePersist
    public void prePersist() {
        if (this.creationDate == null) this.creationDate = new Date();
    }
}

