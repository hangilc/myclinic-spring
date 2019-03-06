package jp.chang.myclinic.backendmysql.entity.core;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by hangil on 2017/06/04.
 */
public interface PharmaQueueRepository extends CrudRepository<PharmaQueue, Integer> {

    Optional<PharmaQueue> findByVisitId(int visitId);

    @Query("select queue, patient from PharmaQueue queue, Visit visit, Patient patient " +
            " where queue.visitId = visit.visitId and visit.patientId = patient.patientId ")
    List<Object[]> findFull();

    List<PharmaQueue> findAll();

    Long deleteByVisitId(int visitId);

}