package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.stream.Stream;

public interface RoujinRepository extends CrudRepository<Roujin, Integer> {

    @Query("select h from Roujin h where h.patientId = ?1 and " +
            " h.validFrom <= date(?2) and " +
            " (h.validUpto = '0000-00-00' or h.validUpto >= date(?2)) "
    )
    Stream<Roujin> findAvailable(int patientId, String at, Sort sort);

    List<Roujin> findByPatientId(int patientId, Sort sort);

    void deleteById(int roujinId);

    Roujin findById(int roujinId);
}