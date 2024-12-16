package itmo.is.lab1.models.dragon;


import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.dragon.requests.DragonCaveRequest;
import itmo.is.lab1.services.dragon.requests.DragonHeadRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "dragon_cave")
public class DragonCave extends GeneralEntity<DragonCaveRequest> {
    private Integer depth;

    @Column(nullable = false)
    @Min(value = 1, message = "Значение numberOfTreasures должно быть больше 0")
    private Integer numberOfTreasures;

    public void setValues(DragonCaveRequest request, User user) {
        super.setValues(request, user);
        this.depth = request.getDepth();
        this.numberOfTreasures = request.getNumberOfTreasures();
    }
}