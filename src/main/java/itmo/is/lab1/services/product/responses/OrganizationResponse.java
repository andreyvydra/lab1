package itmo.is.lab1.services.product.responses;

import itmo.is.lab1.models.product.Organization;
import itmo.is.lab1.models.product.enums.OrganizationType;
import itmo.is.lab1.services.common.responses.GeneralEntityResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrganizationResponse extends GeneralEntityResponse {
    private String name;
    private Long officialAddress;
    private Double annualTurnover;
    private Long employeesCount;
    private String fullName;
    private Double rating;
    private OrganizationType type;

    public void setValues(Organization entity) {
        super.setValues(entity);
        this.name = entity.getName();
        this.annualTurnover = entity.getAnnualTurnover();
        this.employeesCount = entity.getEmployeesCount();
        this.fullName = entity.getFullName();
        this.rating = entity.getRating();
        this.type = entity.getType();
        if (entity.getOfficialAddress() != null) this.officialAddress = entity.getOfficialAddress().getId();
    }
}

