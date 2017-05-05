package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConductRepository extends CrudRepository<Conduct, Integer> {

	List<Conduct> findByVisitId(Integer visitId);
	
}