package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShinryouRepository extends CrudRepository<Shinryou, Integer> {

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where s.shinryouId = :shinryouId and s.visitId = v.visitId " +
		" and s.shinryoucode = m.shinryoucode " + 
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto is null or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findOneWithMaster(@Param("shinryouId") int shinryouId);

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where s.shinryouId in(:shinryouIds) and s.visitId = v.visitId " +
		" and s.shinryoucode = m.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " +
		" and (m.validUpto is null or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findFullByIds(@Param("shinryouIds") List<Integer> shinryouIds);

	@Query("select s, m from Shinryou s, ShinryouMaster m, Visit v " +
		" where v.visitId = :visitId and s.visitId = :visitId " +
		" and s.shinryoucode = m.shinryoucode " +
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto is null or DATE(v.visitedAt) <= m.validUpto)")
	List<Object[]> findByVisitIdWithMaster(@Param("visitId") int visitId, Sort sort);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("delete from Shinryou s where s.shinryouId in :shinryouIds")
    void batchDelete(@Param("shinryouIds") List<Integer> shinryouIds);

	List<Shinryou> findByVisitId(int visitId);

	void deleteById(int shinryouId);

	Shinryou findById(int shinryouId);
}