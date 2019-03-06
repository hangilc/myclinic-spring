package jp.chang.myclinic.backendmysql.entity.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConductShinryouRepository extends CrudRepository<ConductShinryou, Integer> {

	@Query("select s, m from ConductShinryou s, ShinryouMaster m, Conduct c, Visit v " +
		" where c.conductId = :conductId and s.conductId = :conductId " +
		" and v.visitId = c.visitId and m.shinryoucode = s.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByConductIdWithMaster(@Param("conductId") Integer conductId);

	List<ConductShinryou> findByConductId(int conductId, Sort sort);
	List<ConductShinryou> findByConductId(int conductId);

	@Query("select s, m from ConductShinryou s, ShinryouMaster m, Conduct c, Visit v " +
			" where s.conductShinryouId = :conductShinryouId " +
			" and s.conductId = c.conductId and c.visitId = v.visitId " +
			" and m.shinryoucode = s.shinryoucode " +
			" and m.validFrom <= DATE(v.visitedAt) " +
			" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findFull(@Param("conductShinryouId") int conductShinryouId);

	ConductShinryou findById(int conductShinryouId);

	void deleteById(int conductShinryouId);
}