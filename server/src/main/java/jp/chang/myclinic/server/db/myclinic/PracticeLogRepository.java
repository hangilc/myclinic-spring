package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PracticeLogRepository extends CrudRepository<PracticeLog, Integer> {

    List<PracticeLog> findByDate(String date, Sort sort);

    @Query("select p from PracticeLog p where p.date = :date and p.practiceLogId > :id")
    List<PracticeLog> findRecent(@Param("date") String date,
                                 @Param("id") int lastPracticeLogId, Sort sort);

}
