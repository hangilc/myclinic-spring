package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ConductKizaiRepository extends CrudRepository<ConductKizai, Integer> {

	// @EntityGraph(attributePaths={"master"})
	// @Query("select c from ConductKizai c where c.conductId = :conductId")
	// List<ConductKizai> findByConductIdWithMaster(@Param("conductId") Integer conductId);

	@Query("select k, m from ConductKizai k, KizaiMaster m, Conduct c, Visit v " +
		" where c.conductId = :conductId and k.conductId = :conductId " +
		" and v.visitId = c.visitId and m.kizaicode = k.kizaicode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByConductIdWithMaster(@Param("conductId") Integer conductId);

}