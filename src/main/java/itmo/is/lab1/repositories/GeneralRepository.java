package itmo.is.lab1.repositories;

import itmo.is.lab1.models.GeneralEntity;
import itmo.is.lab1.services.common.requests.GeneralRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface GeneralRepository<T> extends JpaRepository<T, Long> {
}
