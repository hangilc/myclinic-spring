package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrescExampleRepository extends CrudRepository<PrescExample, Integer> {

    @Query("select p, m from PrescExample p, IyakuhinMaster m where " +
            " m.iyakuhincode = p.iyakuhincode and m.validFrom = p.masterValidFrom " +
            " and m.name like CONCAT('%', :text, '%') ")
    List<Object[]> searchByNameFull(@Param("text") String text);

    @Query("select p, m from PrescExample p, IyakuhinMaster m where " +
            " m.iyakuhincode = p.iyakuhincode and m.validFrom = p.masterValidFrom ")
    List<Object[]> findAllFull();
}
