package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShinryouRepository extends CrudRepository<Shinryou, Integer> {

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where s.shinryouId = :shinryouId and s.visitId = v.visitId " +
		" and s.shinryoucode = m.shinryoucode " + 
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto) ")
	Object[] findOneWithMaster(@Param("shinryouId") int shinryouId);

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where s.shinryouId in(:shinryouIds) and s.visitId = v.visitId " +
		" and s.shinryoucode = m.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findFullByIds(@Param("shinryouIds") List<Integer> shinryouIds);

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where v.visitId = :visitId and s.visitId = :visitId " +
		" and s.shinryoucode = m.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByVisitIdWithMaster(@Param("visitId") int visitId, Sort sort);
}