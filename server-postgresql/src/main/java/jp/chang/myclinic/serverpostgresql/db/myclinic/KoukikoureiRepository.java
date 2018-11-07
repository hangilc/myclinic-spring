package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public interface KoukikoureiRepository extends CrudRepository<Koukikourei, Integer> {

    @Query("select h from Koukikourei h where h.patientId = ?1 and " +
            " h.validFrom <= ?2 and " +
            " (h.validUpto is null or h.validUpto >= ?2) "
    )
    Stream<Koukikourei> findAvailable(int patientId, LocalDate at, Sort sort);

    List<Koukikourei> findByPatientId(int patientId, Sort sort);

    void deleteById(int koukikoureiId);

    Koukikourei findById(int koukikoureiId);
}