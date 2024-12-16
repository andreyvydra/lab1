package itmo.is.lab1.services.location.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationRequest extends GeneralEntityRequest {
    @NotNull(message = "x не может быть null.")
    private Integer x;
    @NotNull(message = "y не может быть null.")
    private Long y;
    @NotBlank(message = "name не может быть пустым.")
    private String name;

    public Boolean isValid() {
        return x != null && y != null && name != null && this.getIsChangeable() != null;
    }
}
