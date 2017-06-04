package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by hangil on 2017/06/04.
 */
public interface PharmaQueueRepository extends CrudRepository<PharmaQueue, Integer> {

    Optional<PharmaQueue> findByVisitId(int visitId);

}