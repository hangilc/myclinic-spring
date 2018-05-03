package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " and d.endReason = 'N' " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findCurrentWithMaster(@Param("patientId") int patientId, Sort sort);

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findAllWithMaster(@Param("patientId") int patientId, Sort sort);

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findAllWithMaster(@Param("patientId") int patientId, Pageable pageable);

    long countByPatientId(int patientId);

    @Query("select d, m from Disease d, ByoumeiMaster m " +
            " where d.diseaseId = :diseaseId and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= d.startDate " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= d.startDate) ")
    List<Object[]> findFull(@Param("diseaseId") int diseaseId);

    Disease findById(int diseaseId);

    void deleteById(int diseaseId);

    @Query("select d.diseaseId from Disease d where d.patientId = :patientId and " +
            " d.startDate <= :validUpto and " +
            " ( d.endDate = '0000-00-00' or d.endDate >= :validFrom ) ")
    List<Integer> listDiseaseIdByPatientAt(@Param("patientId") int patientId,
                                         @Param("validFrom") String validFrom,
                                         @Param("validUpto") String validupto);
}
