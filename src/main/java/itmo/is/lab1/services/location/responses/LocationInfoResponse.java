package itmo.is.lab1.services.location.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationInfoResponse extends LocationResponse {
    private Long id;
    private String message;
}
