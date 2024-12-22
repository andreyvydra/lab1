package itmo.is.lab1.services.location.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class CoordinatesRequest extends GeneralEntityRequest {
    @NotNull(message = "x не может быть null")
    private Long x; //Поле не может быть null

    @NotNull(message = "y не может быть null")
    @Min(value = -348, message = "Значение поля y должно быть больше -349")
    private Integer y; //Значение поля должно быть больше -349

}
