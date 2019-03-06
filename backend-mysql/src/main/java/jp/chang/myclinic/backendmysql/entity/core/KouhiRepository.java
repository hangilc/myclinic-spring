package jp.chang.myclinic.backendmysql.entity.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface KouhiRepository extends CrudRepository<Kouhi, Integer> {

    @Query("select h from Kouhi h where h.patientId = ?1 and " +
            " h.validFrom <= date(?2) and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= date(?2)) "
    )
    Stream<Kouhi> findAvailable(int patientId, String at, Sort sort);

    List<Kouhi> findByPatientId(int patientId, Sort sort);

    void deleteById(int kouhiId);

    Kouhi findById(int kouhiId);
}