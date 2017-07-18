package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HotlineRepository extends CrudRepository<Hotline, Integer> {

    Optional<Hotline> findTopByOrderByHotlineIdDesc();

    @Query("select h from Hotline h where h.hotlineId >= :lowerHotlineId and h.hotlineId <= :upperHotlineId")
    List<Hotline> findInRange(@Param("lowerHotlineId") int lowerHotlineId, @Param("upperHotlineId") int upperHotlineId, Sort sort);

    @Query("select h from Hotline h where h.postedAt >= CURRENT_DATE()")
    List<Hotline> findTodaysHotline();

    @Query("select h from Hotline h where h.hotlineId > :hotlineId")
    List<Hotline> findRecent(@Param("hotlineId") int thresholdHotlineId);
}
