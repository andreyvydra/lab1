package itmo.is.lab1.services.dragon.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonCaveRequest extends GeneralEntityRequest {
    private Integer depth;

    @NotNull
    @Min(value = 1, message = "Значение numberOfTreasures должно быть больше 0")
    private Integer numberOfTreasures;

}
