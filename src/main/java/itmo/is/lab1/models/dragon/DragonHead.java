package itmo.is.lab1.models.dragon;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.dragon.requests.DragonHeadRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "dragon_head")
public class DragonHead extends GeneralEntity<DragonHeadRequest> {
    @Column(nullable = false)
    private Double size; //Поле не может быть null

    @Column(nullable = false)
    private Long eyesCount; //Поле не может быть null

    @Override
    public void setValues(DragonHeadRequest request, User user) {
        super.setValues(request, user);
        this.setSize(request.getSize());
        this.setEyesCount(request.getEyesCount());
    }
}
