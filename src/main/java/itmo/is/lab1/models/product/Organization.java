package itmo.is.lab1.models.product;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.location.Location;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.models.product.enums.OrganizationType;
import itmo.is.lab1.repositories.AddressRepository;
import itmo.is.lab1.services.product.requests.OrganizationRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
@Table(name = "organization")
public class Organization extends GeneralEntity<OrganizationRequest> {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Address officialAddress;

    @Column(nullable = false)
    private Double annualTurnover;

    private long employeesCount;

    @Column(nullable = false, unique = true)
    private String fullName;

    private Double rating;

    private OrganizationType type;

    public void setValues(OrganizationRequest request, User user, AddressRepository addressRepository) {
        super.setValues(request, user);
        this.name = request.getName();
        if (request.getOfficialAddressId() != null) {
            Optional<Address> address = addressRepository.findById(request.getOfficialAddressId());
            address.ifPresent(value -> officialAddress = value);
        }
        this.annualTurnover = request.getAnnualTurnover();
        this.employeesCount = request.getEmployeesCount();
        this.fullName = request.getFullName();
        this.rating = request.getRating();
        this.type = request.getType();
    }
}
