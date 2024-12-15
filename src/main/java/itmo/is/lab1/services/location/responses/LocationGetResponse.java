package itmo.is.lab1.services.location.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class LocationGetResponse extends LocationResponse {
    private Long id;
    private Integer x;
    private Long y;
    private String name;
    private Boolean isChangeable;
    private Long user;
}