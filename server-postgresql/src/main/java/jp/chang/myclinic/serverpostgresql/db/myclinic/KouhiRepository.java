package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface KouhiRepository extends CrudRepository<Kouhi, Integer> {

    @Query("select h from Kouhi h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto is null or h.validUpto >= ?2) "
    )
    Stream<Kouhi> findAvailable(int patientId, LocalDate at, Sort sort);

    List<Kouhi> findByPatientId(int patientId, Sort sort);

    void deleteById(int kouhiId);

    Kouhi findById(int kouhiId);
}