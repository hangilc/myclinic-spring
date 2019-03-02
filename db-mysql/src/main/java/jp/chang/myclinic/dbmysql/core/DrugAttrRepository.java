package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DrugAttrRepository extends CrudRepository<DrugAttr, Integer> {

    Optional<DrugAttr> findOneByDrugId(int drugId);

    @Query("select s from DrugAttr s where s.drugId in :drugIds")
    List<DrugAttr> batchGetDrugAttr(@Param("drugIds") List<Integer> drugIds);

}
