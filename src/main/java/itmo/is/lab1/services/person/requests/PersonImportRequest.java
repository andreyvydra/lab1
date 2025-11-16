package itmo.is.lab1.services.person.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PersonImportRequest extends PersonRequest {

    @NotNull(message = "Поле 'x' не должно быть null.")
    private Integer x;

    @NotNull(message = "Поле 'y' не должно быть null.")
    private Long y;

    @NotBlank(message = "Поле 'nameLoc' не должно быть пустым.")
    private String nameLoc;
}

