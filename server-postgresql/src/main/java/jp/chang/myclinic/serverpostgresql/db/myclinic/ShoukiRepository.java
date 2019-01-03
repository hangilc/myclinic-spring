package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShoukiRepository extends CrudRepository<Shouki, Integer> {

    Optional<Shouki> findOneByVisitId(int visitId);

    @Query("select s from Shouki s where s.visitId in :visitIds")
    List<Shouki> batchGetShouki(@Param("visitIds") List<Integer> visitIds);

}
