package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChargeRepository extends CrudRepository<Charge, Integer> {

	@Query("select c from Charge c where c.visitId = :visitId")
	Optional<Charge> tryFindByVisitId(@Param("visitId") int visitId);

	Charge findByVisitId(int visitId);

}