package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShinryouRepository extends CrudRepository<Shinryou, Integer> {

	@EntityGraph(attributePaths={"master"})
	@Query("select s from Shinryou s where s.visitId = :visitId")
	List<Shinryou> findByVisitIdWithMaster(@Param("visitId") Integer visitId);

}