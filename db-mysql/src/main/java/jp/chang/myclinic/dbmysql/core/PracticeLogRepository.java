package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PracticeLogRepository extends CrudRepository<PracticeLog, Integer> {

    @Query("select p from PracticeLog p where function('date', p.createdAt) = date(:date)")
    List<PracticeLog> findByDate(LocalDate date, Sort sort);

    @Query("select p from PracticeLog p where function('date', p.createdAt) = date(:date) and p.practiceLogId > :id")
    List<PracticeLog> findRecent(@Param("date") LocalDate date,
                                 @Param("id") int lastPracticeLogId, Sort sort);

    @Query("select p from PracticeLog p where function('date', p.createdAt) = date(:date) " +
            " and p.practiceLogId > :afterId and p.practiceLogId < :beforeId")
    List<PracticeLog> findInRange(@Param("date") LocalDate date,
                                  @Param("afterId") int afterId,
                                  @Param("beforeId") int beforeId,
                                  Sort sort);

    Optional<PracticeLog> findFirstByOrderByPracticeLogIdDesc();
}
