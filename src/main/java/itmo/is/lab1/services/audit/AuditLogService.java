package itmo.is.lab1.services.audit;

import itmo.is.lab1.models.AuditLog;
import itmo.is.lab1.models.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void logAction(User principal, String entityName, Long entityId,
                          String action, Object entity) {
        AuditLog auditLog = AuditLog.builder()
                .entityName(entityName)
                .entityId(entityId)
                .action(action)
                .changedBy(principal.getUsername())
                .changeDate(LocalDateTime.now())
                .details(entity == null ? "null": entity.toString())
                .build();
        entityManager.persist(auditLog);
    }
}