package jp.chang.myclinic.dbmysql;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GazouLabelRepository extends CrudRepository<GazouLabel, Integer> {

	Optional<GazouLabel> findOneByConductId(Integer conductId);

}