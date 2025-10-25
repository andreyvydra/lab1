package itmo.is.lab1.services.product.responses;

import itmo.is.lab1.models.product.Product;
import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.Data;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProductResponse extends GeneralEntityResponse {
    private String name;
    private Long coordinates; // id
    private Date creationDate;
    private UnitOfMeasure unitOfMeasure;
    private Long manufacturer; // id
    private Long price;
    private Integer manufactureCost;
    private int rating;
    private Long owner; // id

    public void setValues(Product entity) {
        super.setValues(entity);
        this.name = entity.getName();
        this.creationDate = entity.getCreationDate();
        this.unitOfMeasure = entity.getUnitOfMeasure();
        this.price = entity.getPrice();
        this.manufactureCost = entity.getManufactureCost();
        this.rating = entity.getRating();
        if (entity.getCoordinates() != null) this.coordinates = entity.getCoordinates().getId();
        if (entity.getManufacturer() != null) this.manufacturer = entity.getManufacturer().getId();
        if (entity.getOwner() != null) this.owner = entity.getOwner().getId();
    }
}
