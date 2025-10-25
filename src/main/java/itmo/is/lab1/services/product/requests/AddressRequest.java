package itmo.is.lab1.services.product.requests;

import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class AddressRequest extends GeneralEntityRequest {
    @NotBlank(message = "Строка street не может быть пустой")
    private String street;
    @NotNull(message = "Поле townId не может быть пустым")
    private Long townId;
}

