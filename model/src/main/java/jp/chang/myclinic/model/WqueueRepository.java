package jp.chang.myclinic.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

public interface WqueueRepository extends CrudRepository<Wqueue, Integer> {

    @Query("select q from Wqueue q where q.waitState in ?1")
    List<Wqueue> findByStateSet(Set<WqueueState> stateSet);
}