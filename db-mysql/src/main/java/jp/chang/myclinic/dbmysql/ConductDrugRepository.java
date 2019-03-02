package jp.chang.myclinic.dbmysql;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConductDrugRepository extends CrudRepository<ConductDrug, Integer> {

	@Query("select d, m from ConductDrug d, IyakuhinMaster m, Conduct c, Visit v " +
		" where d.conductId = :conductId and c.conductId = :conductId " +
		" and v.visitId = c.visitId and d.iyakuhincode = m.iyakuhincode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByConductIdWithMaster(@Param("conductId") int conductId);

	List<ConductDrug> findByConductId(int conductId, Sort sort);
	List<ConductDrug> findByConductId(int conductId);

	@Query("select d, m from ConductDrug d, IyakuhinMaster m, Conduct c, Visit v " +
			" where d.conductDrugId = :conductDrugId and c.conductId = d.conductId " +
			" and v.visitId = c.visitId and d.iyakuhincode = m.iyakuhincode " +
			" and m.validFrom <= DATE(v.visitedAt) " +
			" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findFull(@Param("conductDrugId") int conductDrugId);

	ConductDrug findById(int conductDrugId);

	void deleteById(int conductDrugId);
}