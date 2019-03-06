package jp.chang.myclinic.backendmysql.entity.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShinryouAttrRepository extends CrudRepository<ShinryouAttr, Integer> {

    Optional<ShinryouAttr> findOneByShinryouId(int shinryouId);

    @Query("select s from ShinryouAttr s where s.shinryouId in :shinryouIds")
    List<ShinryouAttr> batchGetShinryouAttr(@Param("shinryouIds") List<Integer> shinryouIds);
}
