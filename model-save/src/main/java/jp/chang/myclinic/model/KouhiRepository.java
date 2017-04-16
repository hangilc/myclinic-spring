package jp.chang.myclinic.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface KouhiRepository extends CrudRepository<Kouhi, Integer> {

    @Query("select h from Kouhi h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= ?2) "
    )
    List<Kouhi> findAvailable(int patientId, Date at);

}