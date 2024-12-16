package itmo.is.lab1.services.location.responses;

import itmo.is.lab1.models.location.Location;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationGetResponse extends LocationResponse {
    private Integer x;
    private Long y;
    private String name;

    public void setValues(Location location) {
        super.setValues(location);
        x = location.getX();
        y = location.getY();
        name = location.getName();
    }
}