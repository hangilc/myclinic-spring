package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.Optional;

public interface KoukikoureiRepository extends CrudRepository<Koukikourei, Integer> {

    @Query("select h from Koukikourei h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= ?2) "
    )
    Optional<Koukikourei> findAvailable(int patientId, Date at);

}