package itmo.is.lab1.repositories;

import itmo.is.lab1.models.location.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends GeneralRepository<Location> {}

