package itmo.is.lab1.services.product.requests;

import itmo.is.lab1.models.product.enums.UnitOfMeasure;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProductRequest extends GeneralEntityRequest {
    @NotNull(message = "Поле name не может быть null")
    @NotBlank(message = "Поле name не может быть пустым")
    private String name;

    @NotNull(message = "Поле coordinates не может быть null")
    private Long coordinates;

    private UnitOfMeasure unitOfMeasure;

    private Long manufacturer;

    @Positive(message = "Поле price должно быть больше 0")
    private Long price;

    private Integer manufactureCost;

    @Positive(message = "Поле rating должно быть больше 0")
    private int rating;

    @NotNull(message = "Поле owner не может быть null")
    private Long owner;
}
