package itmo.is.lab1.repositories;

import itmo.is.lab1.models.user.AdminRequest;
import itmo.is.lab1.models.user.AdminRequestStatus;
import itmo.is.lab1.models.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequest, Long>, JpaSpecificationExecutor<AdminRequest> {
    Page<AdminRequest> findByStatus(AdminRequestStatus status, Pageable pageable);
    List<AdminRequest> findByUser(User user);
}