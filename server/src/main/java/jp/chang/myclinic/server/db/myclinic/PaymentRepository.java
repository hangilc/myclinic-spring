package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    List<Payment> findByVisitIdOrderByPaytimeDesc(int visitIdt);

    Page<Payment> findAll(Pageable pageable);

    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
            " where payment.paytime = (select max(p2.paytime) from Payment p2 where payment.visitId = p2.visitId group by p2.visitId) " +
            " and visit.visitId = payment.visitId and patient.patientId = visit.patientId ")
    List<Object[]> findAllFull(Pageable pageable);

    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
            " where payment.paytime = (select max(p2.paytime) from Payment p2 where payment.visitId = p2.visitId group by p2.visitId) " +
            " and visit.visitId = payment.visitId and patient.patientId = visit.patientId and visit.patientId = :patientId ")
    Page<Object[]> findFullByPatient(@Param("patientId") int patientId, Pageable pageable);

    @Query("select payment from Payment payment where payment.paytime = " +
            " (select max(p2.paytime) from Payment p2 where p2.visitId = payment.visitId group by p2.visitId) ")
    List<Payment> findFinalPayment(Pageable pageable);

    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
            " where payment.paytime = (select max(p2.paytime) from Payment p2 where payment.visitId = p2.visitId group by p2.visitId) " +
            " and visit.visitId = payment.visitId and patient.patientId = visit.patientId " +
            " and payment.visitId in :visitIds ")
    List<Object[]> findFullFinalPayment(@Param("visitIds") List<Integer> visitIds, Pageable pageable);

    List<Payment> findByVisitId(int visitId);
}