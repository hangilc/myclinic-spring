package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {

    @EntityGraph(attributePaths = {"master"})
    @Query("select d from Disease d where d.patientId = ?1")
    List<Disease> findAllByPatientIdWithMaster(int patientId);

    @EntityGraph(attributePaths = {"master"})
    @Query("select d from Disease d where d.patientId = ?1 and d.endReason = 'N'")
    List<Disease> findCurrentByPatientIdWithMaster(int patientId);
}