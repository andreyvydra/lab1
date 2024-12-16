package itmo.is.lab1.services.dragon.responses;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.dragon.DragonHead;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonHeadResponse extends GeneralEntityResponse {
    private Double size;
    private Long eyesCount;

    public void setValues(DragonHead entity) {
        super.setValues(entity);
        size = entity.getSize();
        eyesCount = entity.getEyesCount();
    }
}
