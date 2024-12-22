package itmo.is.lab1.services.common.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class GeneralEntityRequest extends GeneralRequest {
    @NotNull(message = "isChangeable не может быть null")
    private Boolean isChangeable;

}
