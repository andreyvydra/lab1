package itmo.is.lab1.services.dragon.responses;

import itmo.is.lab1.models.dragon.DragonHead;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonHeadResponse extends GeneralEntityResponse {
    @NotNull(message = "size не может быть null.")
    private Double size;
    @NotNull(message = "eyesCount не может быть null.")
    private Long eyesCount;

    public void setValues(DragonHead entity) {
        super.setValues(entity);
        size = entity.getSize();
        eyesCount = entity.getEyesCount();
    }
}
