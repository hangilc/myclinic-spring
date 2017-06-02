package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    List<Payment> findByVisitIdOrderByPaytimeDesc(int visitIdt);

    Page<Payment> findAll(Pageable pageable);

//    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
//        " where payment.visitId = visit.visitId and patient.patientId = visit.patientId ")
    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
            " where payment.paytime = (select max(p2.paytime) from Payment p2 where payment.visitId = p2.visitId group by p2.visitId) " +
            " and visit.visitId = payment.visitId and patient.patientId = visit.patientId ")
    Page<Object[]> findAllFull(Pageable pageable);

    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
            " where payment.paytime = (select max(p2.paytime) from Payment p2 where payment.visitId = p2.visitId group by p2.visitId) " +
            " and visit.visitId = payment.visitId and patient.patientId = visit.patientId and visit.patientId = :patientId ")
    Page<Object[]> findFullByPatient(@Param("patientId") int patientId, Pageable pageable);
}