package itmo.is.lab1.repositories;

import itmo.is.lab1.models.dragon.Dragon;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DragonRepository extends GeneralRepository<Dragon> {
    List<Dragon> findByAge(Integer age);

    @Query("SELECT DISTINCT d.speaking FROM Dragon d")
    List<Boolean> findDistinctSpeakingValues();
}
