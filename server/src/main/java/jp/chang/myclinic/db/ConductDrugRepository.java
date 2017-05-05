package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ConductDrugRepository extends CrudRepository<ConductDrug, Integer> {

	@EntityGraph(attributePaths={"master"})
	@Query("select c from ConductDrug c where c.conductId = :conductId")
	List<ConductDrug> findByConductIdWithMaster(@Param("conductId") Integer conductId);

}