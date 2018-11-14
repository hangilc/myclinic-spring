package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GazouLabelRepository extends CrudRepository<GazouLabel, Integer> {

	Optional<GazouLabel> findOneByConductId(Integer conductId);

}