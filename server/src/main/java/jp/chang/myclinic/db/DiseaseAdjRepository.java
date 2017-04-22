package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiseaseAdjRepository extends CrudRepository<DiseaseAdj, Integer> {

    @EntityGraph(attributePaths = {"master"})
    @Query("select d from DiseaseAdj d where d.diseaseId = ?1")
    List<DiseaseAdj> findByDiseaseIdWithMaster(int diseaseId);
}