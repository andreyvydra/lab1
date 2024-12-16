package itmo.is.lab1.services.common.responses;

import itmo.is.lab1.models.GeneralEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class GeneralEntityResponse extends GeneralResponse {
    private Long id;
    private Boolean isChangeable;
    private Long user;

    public void setValues(GeneralEntity<?> entity) {
        id = entity.getId();
        isChangeable = entity.getIsChangeable();
        user = entity.getUser().getId();
    }
}
