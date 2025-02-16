package itmo.is.lab1.controllers;

import itmo.is.lab1.models.user.AdminRequestStatus;
import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.user.AdminRequestService;
import itmo.is.lab1.services.user.responses.AdminRequestResponse;
import itmo.is.lab1.specification.PaginatedResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin-requests")
@RequiredArgsConstructor
public class AdminRequestController {

    private final AdminRequestService adminRequestService;

    @PostMapping
    public AdminRequestResponse createAdminRequest(
            @AuthenticationPrincipal User user) {
        return adminRequestService.createAdminRequest(user);
    }

    @GetMapping
    public PaginatedResponse<AdminRequestResponse> getAllAdminRequests(
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "true") @NotNull Boolean ascending,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1) @Max(value = 50) Integer size
    ) {
        return adminRequestService.getAllAdminRequests(sortField, ascending, page, size);
    }

    @GetMapping("/status/{status}")
    public PaginatedResponse<AdminRequestResponse> getAdminRequestsByStatus(
            @PathVariable AdminRequestStatus status,
            @RequestParam(required = false, defaultValue = "id") String sortField,
            @RequestParam(required = false, defaultValue = "true") @NotNull Boolean ascending,
            @RequestParam(required = false, defaultValue = "0") @Min(value = 0) Integer page,
            @RequestParam(required = false, defaultValue = "10") @Min(value = 1) @Max(value = 50) Integer size) {
        return adminRequestService.getAdminRequestsByStatus(status, sortField, ascending, page, size);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveAdminRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        adminRequestService.changeStatus(id, admin, AdminRequestStatus.APPROVED);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectAdminRequest(
            @PathVariable Long id,
            @AuthenticationPrincipal User admin) {
        adminRequestService.changeStatus(id, admin, AdminRequestStatus.REJECTED);
        return ResponseEntity.ok().build();
    }
}