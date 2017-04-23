package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public interface WqueueRepository extends CrudRepository<Wqueue, Integer> {

    @Query("select q from Wqueue q where q.waitState in ?1")
    List<Wqueue> findByStateSet(Set<Integer> stateSet);

    @Query("select q from Wqueue q")
    Stream<Wqueue> findAllAsStream();
}