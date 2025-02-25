package itmo.is.lab1.controllers;

import itmo.is.lab1.models.ImportHistory;
import itmo.is.lab1.services.import_history.ImportHistoryService;
import itmo.is.lab1.services.import_history.responses.ImportHistoryResponse;
import itmo.is.lab1.services.user.UserService;
import itmo.is.lab1.specification.PaginatedResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/import-history")
public class ImportHistoryController {

    @Autowired
    private ImportHistoryService importHistoryService;
    @Autowired
    private UserService userService;

    @GetMapping
    public PaginatedResponse<ImportHistoryResponse> getImportHistory(
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "true") @NotNull Boolean ascending,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1) @Max(value = 50) Integer size
    ) {
        return importHistoryService.getAllHistory(filter, sortField, ascending, page, size);
    }
}