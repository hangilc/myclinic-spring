package jp.chang.myclinic.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ConductKizaiRepository extends CrudRepository<ConductKizai, Integer> {

	@EntityGraph(attributePaths={"master"})
	@Query("select c from ConductKizai c where c.conductId = :conductId")
	List<ConductKizai> findByConductIdWithMaster(@Param("conductId") Integer conductId);

}