package jp.chang.myclinic.db.myclinic;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConductShinryouRepository extends CrudRepository<ConductShinryou, Integer> {

	// @EntityGraph(attributePaths={"master"})
	// @Query("select c from ConductShinryou c where c.conductId = :conductId")
	// List<ConductShinryou> findByConductIdWithMaster(@Param("conductId") Integer conductId);

	@Query("select s, m from ConductShinryou s, ShinryouMaster m, Conduct c, Visit v " +
		" where c.conductId = :conductId and s.conductId = :conductId " +
		" and v.visitId = c.visitId and m.shinryoucode = s.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByConductIdWithMaster(@Param("conductId") Integer conductId);

}