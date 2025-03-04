package itmo.is.lab1.services.audit;

import itmo.is.lab1.models.user.User;
import itmo.is.lab1.services.jwt.AuthenticationService;
import itmo.is.lab1.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;


@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditAspect {
    private final AuditLogService auditLogService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @Around("@annotation(auditable)")
    public Object logAudit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {

        Object res;

        try {
            res = joinPoint.proceed();
        } catch (Throwable e) {
            log.warn("Join Point cannot be proceed");
            throw e;
        }

        try {
            Object[] args = joinPoint.getArgs();
            Long entityId = null;
            Object entity = null;
            User principal = userService.getCurrentUser();

            for (Object arg : args) {
                if (arg instanceof Long) {
                    entityId = (Long) arg;
                } else {
                    entity = arg;
                    if (entityId == null) {
                        entityId = getIdFromEntity(entity);
                    }
                }
            }

            if (entityId != null) {
                auditLogService.logAction(
                        principal,
                        auditable.entityType(),
                        entityId,
                        auditable.action(),
                        entity
                );
            } else {
                log.warn("Не удалось определить ID сущности для аудита.");
            }
        } catch (UsernameNotFoundException e) {
            log.warn("Текущий пользователь не аутентифицирован.");
        } catch (Exception e) {
            log.error("Auditing failed", e);
            throw e;
        }
        return res;
    }


    private Long getIdFromEntity(Object entity) {
        try {
            return (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            log.error("Ошибка при попытке получить ID: ", e);
            return null;
        }
    }
}