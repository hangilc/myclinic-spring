package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConductDrugRepository extends CrudRepository<ConductDrug, Integer> {

	// @EntityGraph(attributePaths={"master"})
	// @Query("select c from ConductDrug c where c.conductId = :conductId")
	// List<ConductDrug> findByConductIdWithMaster(@Param("conductId") Integer conductId);

	@Query("select d, m from ConductDrug d, IyakuhinMaster m, Conduct c, Visit v " +
		" where d.conductId = :conductId and c.conductId = :conductId " +
		" and v.visitId = c.visitId and d.iyakuhincode = m.iyakuhincode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByConductIdWithMaster(@Param("conductId") int conductId);

	List<ConductDrug> findByConductId(int conductId, Sort sort);
	List<ConductDrug> findByConductId(int conductId);

}