package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugRepository extends CrudRepository<Drug, Integer> {

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

	List<Drug> findByVisitId(int visitId, Sort sort);

	@Query("select d.iyakuhincode from Drug d, Visit v where d.visitId = v.visitId and v.patientId = :patientId")
	List<Integer> findIyakuhincodeByPatient(@Param("patientId") int patientId);

	@Query("select d.visitId, v.visitedAt from Drug d, Visit v where v.patientId = :patientId and v.visitId = d.visitId " +
			" and d.iyakuhincode = :iyakuhincode")
	List<Object[]> findVisitIdVisitedAtByPatientAndIyakuhincode(@Param("patientId") int patientId,
																@Param("iyakuhincode") int iyakuhincode);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Drug d set d.prescribed = 1 where d.visitId = :visitId")
	void markAsPrescribedForVisit(@Param("visitId") int visitId);

	@Query("select MAX(d.drugId) from Drug d, Visit v where d.visitId = v.visitId " +
			" and v.patientId = :patientId " +
			" and d.category in (0, 1) " +
			" group by d.iyakuhincode, d.amount, d.usage, d.days ")
	List<Integer> findNaifukuAndTonpukuPatternByPatient(@Param("patientId") int patientId);

	@Query("select MAX(d.drugId) from Drug d, Visit v, IyakuhinMaster m where d.visitId = v.visitId " +
			" and v.patientId = :patientId " +
			" and d.iyakuhincode = m.iyakuhincode " +
			" and m.validFrom <= DATE(v.visitedAt) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(v.visitedAt)) " +
			" and m.name like CONCAT('%', :text, '%') " +
			" and d.category in (0, 1) " +
			" group by d.iyakuhincode, d.amount, d.usage, d.days ")
	List<Integer> findNaifukuAndTonpukuPatternByPatient(@Param("patientId") int patientId, @Param("text") String text);

	@Query("select MAX(d.drugId) from Drug d, Visit v where d.visitId = v.visitId " +
			" and v.patientId = :patientId " +
			" and d.category = 2 " +
			" group by d.iyakuhincode, d.amount, d.usage, d.days ")
	List<Integer> findGaiyouPatternByPatient(@Param("patientId") int patientId);

	@Query("select MAX(d.drugId) from Drug d, Visit v, IyakuhinMaster m where d.visitId = v.visitId " +
			" and v.patientId = :patientId " +
			" and d.iyakuhincode = m.iyakuhincode " +
			" and m.validFrom <= DATE(v.visitedAt) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(v.visitedAt)) " +
			" and m.name like CONCAT('%', :text, '%') " +
			" and d.category = 2 " +
			" group by d.iyakuhincode, d.amount, d.usage, d.days ")
	List<Integer> findGaiyouPatternByPatient(@Param("patientId") int patientId, @Param("text") String text);

	int countByVisitIdAndPrescribed(int visitId, int prescribed);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("update Drug d set d.days = :days where d.drugId in :drugIds")
	void batchUpdateDays(@Param("drugIds") List<Integer> drugIds, @Param("days") int days);

	@Query("select drug.visitId from Drug drug, Visit visit where drug.iyakuhincode = :iyakuhincode " +
			" and visit.patientId = :patientId and visit.visitId = drug.visitId ")
	Page<Integer> pageVisitIdsByPatientAndIyakuhincode(@Param("patientId") int patientId,
													   @Param("iyakuhincode") int iyakuhincode,
													   Pageable pageable);


	Drug findById(int drugId);

	void deleteById(int drugId);
}