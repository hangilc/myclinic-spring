package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	@Query("select visit.visitId from Visit visit where date(visit.visitedAt) = date(now()) ")
	List<Integer> findVisitIdForToday(Sort sort);

	@Query("select visit, patient from Visit visit, Patient patient " +
			" where visit.patientId = patient.patientId and visit.visitId in :visitIds ")
	List<Object[]> findByVisitIdsWithPatient(@Param("visitIds") List<Integer> visitIds);

	int countByPatientId(int patientId);

	@Query("select v.visitId from Visit v")
    List<Integer> findAllVisitIds(Sort sort);

    @Query("select v, p from Visit v, Patient p where v.patientId = p.patientId")
    List<Object[]> findAllWithPatient(Pageable pageable);

    @Query("select visit.visitId from Visit visit where visit.patientId = :patientId")
	List<Integer> findVisitIdsByPatient(@Param("patientId") int patientId, Sort sort);

    @Query("select visit.visitId, visit.visitedAt from Visit visit where visit.patientId = :patientId")
	List<Object[]> findVisitIdVisitedAtByPatient(@Param("patientId") int patientId, Sort sort);

	@Query("select visit from Visit visit where visit.visitId in :visitIds")
	List<Visit> findByVisitIds(@Param("visitIds") List<Integer> visitIds, Sort sort);

	Page<Visit> findByPatientId(int patientId, Pageable pageable);

	@Query("select v, p from Visit v, Patient p where v.visitId in :visitIds and p.patientId = v.patientId")
	List<Object[]> findWithPatient(@Param("visitIds") List<Integer> visitIds, Sort sort);
}