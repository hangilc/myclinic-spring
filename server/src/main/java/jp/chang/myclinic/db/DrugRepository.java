package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

public interface DrugRepository extends CrudRepository<Drug, Integer> {

	// @EntityGraph(attributePaths={"master"})
	// @Query("select d from Drug d where d.visitId = :visitId")
	// List<Drug> findByVisitIdWithMaster(@Param("visitId") Integer visitId);

	@Query("select d, m from Drug d, IyakuhinMaster m, Visit v " +
		" where d.drugId = :drugId and d.visitId = v.visitId " +
		" and d.iyakuhincode = m.iyakuhincode " + 
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findOneWithMaster(@Param("drugId") int drugId);

	@Query("select d, m from Drug d, IyakuhinMaster m, Visit v " +
		" where d.visitId = :visitId and d.visitId = v.visitId " +
		" and d.iyakuhincode = m.iyakuhincode " + 
		" and m.validFrom <= DATE(v.visitedAt) " + 
		" and (m.validUpto = '0000-00-00' or DATE(v.visitedAt) <= m.validUpto) ")
	List<Object[]> findByVisitIdWithMaster(@Param("visitId") int visitId, Sort sort);

	@Query("select d.iyakuhincode from Drug d, Visit v where d.visitId = v.visitId and v.patientId = :patientId")
	List<Integer> findIyakuhincodeByPatient(@Param("patientId") int patientId);

}