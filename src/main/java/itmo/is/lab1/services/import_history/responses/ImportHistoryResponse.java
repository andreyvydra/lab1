package itmo.is.lab1.services.import_history.responses;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ImportHistoryResponse {
    private Long id;
    private Long user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private Integer addedObjects;
}
