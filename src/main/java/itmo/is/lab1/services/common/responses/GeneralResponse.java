package itmo.is.lab1.services.common.responses;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GeneralResponse {
    private String message;
}
