package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ByoumeiMasterRepository extends CrudRepository<ByoumeiMaster, ByoumeiMasterId> {

    @Query("select m from ByoumeiMaster m where m.name like CONCAT('%', :text, '%') " +
            " and m.validFrom <= DATE(:at) and (m.validUpto is null or m.validUpto >= DATE(:at)) ")
    List<ByoumeiMaster> searchByName(@Param("text") String text, @Param("at") LocalDate at);

    @Query("select m from ByoumeiMaster m where m.name = :name " +
            " and m.validFrom <= DATE(:at) and (m.validUpto is null or m.validUpto >= DATE(:at)) ")
    Optional<ByoumeiMaster> findByName(@Param("name") String name, @Param("at") LocalDate at);

}

