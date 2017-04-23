package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.stream.Stream;

public interface KouhiRepository extends CrudRepository<Kouhi, Integer> {

    @Query("select h from Kouhi h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= ?2) "
    )
    Stream<Kouhi> findAvailable(int patientId, Date at, Sort sort);

}