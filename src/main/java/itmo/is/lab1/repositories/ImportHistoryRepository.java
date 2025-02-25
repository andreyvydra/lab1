package itmo.is.lab1.repositories;

import itmo.is.lab1.models.ImportHistory;
import itmo.is.lab1.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ImportHistoryRepository extends JpaRepository<ImportHistory, Long>, JpaSpecificationExecutor<ImportHistory> {
    Page<ImportHistory> findByUser(User user, Specification<?> specification, Pageable pageable);
    Page<ImportHistory> findAll(Specification<ImportHistory> specification, Pageable pageable);
}