package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface ShahokokuhoRepository extends CrudRepository<Shahokokuho, Integer> {

    @Query("select h from Shahokokuho h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto is null or h.validUpto >= ?2) "
    )
    Stream<Shahokokuho> findAvailable(int patientId, LocalDate at, Sort sort);

    List<Shahokokuho> findByPatientId(int patientId, Sort sort);

    void deleteById(int shahokokuhoId);

    Shahokokuho findById(int shahokokuhoId);
}