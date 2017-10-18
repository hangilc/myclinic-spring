package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiseaseAdjRepository extends CrudRepository<DiseaseAdj, Integer> {

    @Query("select a, m from DiseaseAdj a, ShuushokugoMaster m where a.diseaseId = :diseaseId " +
            " and a.shuushokugocode = m.shuushokugocode ")
    List<Object[]> findByDiseaseIdWithMaster(@Param("diseaseId") int diseaseId, Sort sort);

    void deleteByDiseaseId(int diseaseId);
}
