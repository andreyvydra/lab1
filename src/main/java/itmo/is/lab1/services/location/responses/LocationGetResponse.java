package itmo.is.lab1.services.location.responses;

import itmo.is.lab1.models.location.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationGetResponse extends LocationResponse {
    @NotNull(message = "x не может быть null.")
    private Integer x;
    @NotNull(message = "y не может быть null.")
    private Long y;
    private String name;
    private long z;

    public void setValues(Location location) {
        super.setValues(location);
        x = location.getX();
        y = location.getY();
        name = location.getName();
        z = location.getZ();
    }
}