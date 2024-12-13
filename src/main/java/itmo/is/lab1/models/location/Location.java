package itmo.is.lab1.models.location;


import itmo.is.lab1.models.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "x не может быть null.")
    private Integer x; //Поле не может быть null

    @NotNull(message = "y не может быть null.")
    private Long y; //Поле не может быть null

    @NotBlank(message = "name не может быть пустым.")
    private String name; //Строка не может быть пустой, Поле может быть null

    @NotNull(message = "isChangeable не может быть null.")
    private Boolean isChangeable;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
