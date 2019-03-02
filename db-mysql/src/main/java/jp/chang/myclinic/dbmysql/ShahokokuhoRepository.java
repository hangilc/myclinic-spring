package jp.chang.myclinic.dbmysql;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface ShahokokuhoRepository extends CrudRepository<Shahokokuho, Integer> {

    @Query("select h from Shahokokuho h where h.patientId = ?1 and " +
            " h.validFrom <= date(?2) and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= date(?2)) "
    )
    Stream<Shahokokuho> findAvailable(int patientId, String at, Sort sort);

    List<Shahokokuho> findByPatientId(int patientId, Sort sort);

    void deleteById(int shahokokuhoId);

    Shahokokuho findById(int shahokokuhoId);
}