package itmo.is.lab1.services.dragon.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonHeadRequest extends GeneralEntityRequest {
    private Double size;
    private Long eyesCount;

    @Override
    public Boolean isValid() {
        return size != null && eyesCount != null && this.getIsChangeable() != null;
    }
}