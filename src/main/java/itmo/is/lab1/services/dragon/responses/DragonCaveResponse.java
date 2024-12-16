package itmo.is.lab1.services.dragon.responses;

import itmo.is.lab1.models.dragon.DragonCave;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class DragonCaveResponse extends GeneralEntityResponse {
    private Integer depth;

    @NotNull
    @Min(value = 1, message = "Значение numberOfTreasures должно быть больше 0")
    private Integer numberOfTreasures;

    public void setValues(DragonCave entity) {
        super.setValues(entity);
        depth = entity.getDepth();
        numberOfTreasures = entity.getNumberOfTreasures();
    }
}
