package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ConductShinryouRepository extends CrudRepository<ConductShinryou, Integer> {

	@EntityGraph(attributePaths={"master"})
	@Query("select c from ConductShinryou c where c.conductId = :conductId")
	List<ConductShinryou> findByConductIdWithMaster(@Param("conductId") Integer conductId);

}