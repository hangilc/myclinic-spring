package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChargeRepository extends CrudRepository<Charge, Integer> {

	@Query("select c from Charge c where c.visitId = :visitId")
	Optional<Charge> findByVisitId(@Param("visitId") int visitId);

	Charge findById(int visitId);
}