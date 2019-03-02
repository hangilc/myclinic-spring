package jp.chang.myclinic.dbmysql;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
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

	List<ConductKizai> findByConductId(int conductId, Sort sort);
	List<ConductKizai> findByConductId(int conductId);

	@Query("select k, m from ConductKizai k, KizaiMaster m, Conduct c, Visit v " +
			" where k.conductKizaiId = :conductKizaiId and c.conductId = k.conductId " +
			" and v.visitId = c.visitId and m.kizaicode = k.kizaicode " +
			" and m.validFrom <= DATE(v.visitedAt) " +
			" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findFull(@Param("conductKizaiId") int conductKizaiId);

	ConductKizai findById(int conductKizaiId);

	void deleteById(int conductKizaiId);
}