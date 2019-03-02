package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ByoumeiMasterRepository extends CrudRepository<ByoumeiMaster, ByoumeiMasterId> {

    @Query("select m from ByoumeiMaster m where m.name like CONCAT('%', :text, '%') " +
            " and m.validFrom <= DATE(:at) and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at)) ")
    List<ByoumeiMaster> searchByName(@Param("text") String text, @Param("at") Date at);

    @Query("select m from ByoumeiMaster m where m.name = :name " +
            " and m.validFrom <= DATE(:at) and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at)) ")
    Optional<ByoumeiMaster> findByName(@Param("name") String name, @Param("at") Date at);

}

