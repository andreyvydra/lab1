package itmo.is.lab1.models;

import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public abstract class GeneralEntity<R extends GeneralEntityRequest> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "isChangeable не может быть null")
    @Column(nullable = false)
    private Boolean isChangeable;

    @NotNull(message = "user не может быть null")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void setValues(R request, User user) {
        isChangeable = request.getIsChangeable();
        this.user = user;
    }
}
