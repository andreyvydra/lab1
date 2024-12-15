package itmo.is.lab1.services.location.requests;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LocationRequest {
    private Integer x;
    private Long y;
    private String name;
    private Boolean isChangeable;
    private Long user;

    public Boolean isValid() {
        return x != null && y != null && name != null && isChangeable != null;
    }
}
