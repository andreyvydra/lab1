package itmo.is.lab1.services.user.responses;

import itmo.is.lab1.models.user.AdminRequestStatus;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@Builder
public class AdminRequestResponse {
    private Long id;
    private Long userId;
    private AdminRequestStatus status;
    private LocalDateTime createdAt;
    private Long reviewedByUserId;
}