package itmo.is.lab1.services.common.requests;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public abstract class GeneralEntityRequest extends GeneralRequest {
    private Boolean isChangeable;

    public abstract Boolean isValid();
}
