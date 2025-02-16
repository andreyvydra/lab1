package itmo.is.lab1.models.user;

public enum AdminRequestStatus {
    PENDING, // Заявка ожидает рассмотрения
    APPROVED, // Заявка одобрена
    REJECTED // Заявка отклонена
}