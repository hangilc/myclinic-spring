package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " d.endReason = 'N' and m.shoubyoumeicode = m.shoubyoumeicode " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate ")
    List<Object[]> findCurrentWithMaster(@Param("patientId") int patientId, Sort sort);

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " and m.shoubyoumeicode = m.shoubyoumeicode " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findAllWithMaster(@Param("patientId") int patientId, Pageable pageable);

    long countByPatientId(int patientId);

    @Query("select d, m from Disease d, ByoumeiMaster m, Visit v " +
            " where d.diseaseId = :diseaseId and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findFull(@Param("diseaseId") int diseaseId);

}
