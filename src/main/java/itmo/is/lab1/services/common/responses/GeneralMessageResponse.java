package itmo.is.lab1.services.common.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class GeneralMessageResponse extends  GeneralResponse {
    private String message;
}
