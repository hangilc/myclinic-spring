package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShuushokugoMasterRepository extends CrudRepository<ShuushokugoMaster, Integer>  {

    @Query("select m from ShuushokugoMaster m where m.name like CONCAT('%', :text, '%') ")
    List<ShuushokugoMaster> searchByName(@Param("text")String text);
}
