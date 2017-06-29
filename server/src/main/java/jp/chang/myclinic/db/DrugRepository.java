package jp.chang.myclinic.db;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

	@Query("select d.visitId, v.visitedAt from Drug d, Visit v where v.patientId = :patientId and v.visitId = d.visitId " +
			" and d.iyakuhincode = :iyakuhincode")
	List<Object[]> findVisitIdVisitedAtByPatientAndIyakuhincode(@Param("patientId") int patientId, @Param("iyakuhincode") int iyakuhincode);

}