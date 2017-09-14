package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConductRepository extends CrudRepository<Conduct, Integer> {

	@Query("select c from Conduct c where c.conductId in :conductIds")
    List<Conduct> listConductByIds(@Param("conductIds") List<Integer> conductIds, Sort sort);

	List<Conduct> findByVisitId(int visitId, Sort sort);
}