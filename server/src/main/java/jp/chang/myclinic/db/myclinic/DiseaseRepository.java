package jp.chang.myclinic.db.myclinic;

import org.springframework.data.repository.CrudRepository;

public interface DiseaseRepository extends CrudRepository<Disease, Integer> {
}
