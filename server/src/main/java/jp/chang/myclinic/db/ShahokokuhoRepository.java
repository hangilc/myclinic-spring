package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.stream.Stream;

public interface ShahokokuhoRepository extends CrudRepository<Shahokokuho, Integer> {

    @Query("select h from Shahokokuho h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= ?2) "
    )
    Stream<Shahokokuho> findAvailable(int patientId, Date at, Sort sort);

}