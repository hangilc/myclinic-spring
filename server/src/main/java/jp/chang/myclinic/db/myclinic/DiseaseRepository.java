package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {

    @Query("select d, m from Disease d, ByoumeiMaster m where d.patientId = :patientId " +
            " and d.endReason = 'N' and m.shoubyoumeicode = m.shoubyoumeicode " +
            " and m.shoubyoumeicode = d.shoubyoumeicode " +
            " and m.validFrom <= DATE(:at) " +
            " and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at)) ")
    List<Object[]> findCurrentWithMaster(@Param("patientId") int patientId, @Param("at") Date at, Sort sort);
}
