package itmo.is.lab1.services.product.responses;

import itmo.is.lab1.models.product.Address;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddressResponse extends GeneralEntityResponse {
    private String street;
    private Long town;

    public void setValues(Address entity) {
        super.setValues(entity);
        this.street = entity.getStreet();
        if (entity.getTown() != null) this.town = entity.getTown().getId();
    }
}

