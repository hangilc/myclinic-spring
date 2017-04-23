package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

	@EntityGraph(attributePaths={"patient"})
	@Query("select v from Visit v")
	List<Visit> findAllWithPatient(Pageable pageable);

	@EntityGraph(attributePaths={"patient"})
	@Query("select v from Visit v where FUNCTION('date', v.visitedAt) = current_date")
	List<Visit> findTodaysVisits(Pageable pageable);

	int countByPatientId(int patientId);

	List<Visit> findByPatientId(int patientId, Pageable pageable);
}