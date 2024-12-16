package itmo.is.lab1.services.common.requests;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class GeneralRequest {
    public abstract Boolean isValid();
}
