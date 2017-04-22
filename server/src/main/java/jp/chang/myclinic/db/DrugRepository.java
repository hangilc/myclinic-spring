package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DrugRepository extends CrudRepository<Drug, Integer> {

	@EntityGraph(attributePaths={"master"})
	@Query("select d from Drug d where d.visitId = :visitId")
	List<Drug> findByVisitIdWithMaster(@Param("visitId") Integer visitId);

}