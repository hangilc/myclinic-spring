package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShinryouRepository extends CrudRepository<Shinryou, Integer> {

	// @EntityGraph(attributePaths={"master"})
	// @Query("select s from Shinryou s where s.visitId = :visitId")
	// List<Shinryou> findByVisitIdWithMaster(@Param("visitId") Integer visitId);

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where s.shinryouId = :shinryouId and s.visitId = v.visitId " +
		" and s.shinryoucode = m.shinryoucode " + 
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findOneWithMaster(@Param("shinryouId") int shinryouId);

}