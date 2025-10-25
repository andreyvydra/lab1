package itmo.is.lab1.services.product.requests;

import itmo.is.lab1.models.product.enums.OrganizationType;
import itmo.is.lab1.services.common.requests.GeneralEntityRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrganizationRequest extends GeneralEntityRequest {
    @NotNull(message = "Поле name не может быть null")
    @NotBlank(message = "Поле name не может быть пустой")
    private String name;

    private Long officialAddressId;

    @NotNull(message = "Поле annualTurnover не может быть null")
    @Positive(message = "Значение поля annualTurnover должно быть больше 0")
    private Double annualTurnover;

    @Positive(message = "Значение поля employeesCount должно быть больше 0")
    private Long employeesCount;

    @Size(max = 1974)
    @NotNull(message = "Поле fullName не может быть null")
    private String fullName;

    @Positive(message = "Значение поля rating должно быть больше 0")
    private Double rating;

    @NotNull(message = "Поле type не может быть null")
    private OrganizationType type;
}

