package jp.chang.myclinic.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.Optional;

public interface RoujinRepository extends CrudRepository<Roujin, Integer> {

    @Query("select h from Roujin h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= ?2) "
    )
    Optional<Roujin> findAvailable(int patientId, Date at);

}