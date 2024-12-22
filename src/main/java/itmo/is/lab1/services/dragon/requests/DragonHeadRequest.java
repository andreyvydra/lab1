package itmo.is.lab1.services.dragon.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonHeadRequest extends GeneralEntityRequest {
    @NotNull(message = "size не может быть null.")
    private Double size;
    @NotNull(message = "eyesCount не может быть null.")
    private Long eyesCount;

}