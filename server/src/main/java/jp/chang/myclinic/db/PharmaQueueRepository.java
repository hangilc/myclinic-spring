package jp.chang.myclinic.db;

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
            " where queue.visitId = visit.visitId and visit.patientId = patient.patientId " +
            " and queue.pharmaState = 0 ")
    List<Object[]> findFullForPrescription();

    @Query("select queue, patient from PharmaQueue queue, Visit visit, Patient patient " +
            " where queue.visitId = visit.visitId and visit.patientId = patient.patientId " +
            " and date(visit.visitedAt) = date(now()) ")
    List<Object[]> findFullForToday();

    @Query("select queue, patient from PharmaQueue queue, Visit visit, Patient patient " +
            " where queue.visitId = visit.visitId and visit.patientId = patient.patientId ")
    List<Object[]> findFull();
}