package itmo.is.lab1.services.location.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import itmo.is.lab1.services.common.requests.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationRequest extends GeneralEntityRequest {
    private Integer x;
    private Long y;
    private String name;

    public Boolean isValid() {
        return x != null && y != null && name != null && this.getIsChangeable() != null;
    }
}
