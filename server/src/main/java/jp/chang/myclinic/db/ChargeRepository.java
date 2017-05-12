package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface ChargeRepository extends CrudRepository<Charge, Integer> {

	Optional<Charge> findByVisitId(int visitId);

}