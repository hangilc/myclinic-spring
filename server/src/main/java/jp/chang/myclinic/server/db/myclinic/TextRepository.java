package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by hangil on 2017/06/07.
 */
public interface TextRepository extends CrudRepository<Text, Integer> {

    List<Text> findByVisitId(int visitId);

    @Query("select text, visit from Text text, Visit visit " +
            " where visit.patientId = :patientId and text.visitId = visit.visitId " + "" +
            " and text.content like CONCAT('%', :text, '%') ")
    Page<Object[]> searchText(@Param("patientId") int patientId, @Param("text") String text, Pageable pageable);
}
