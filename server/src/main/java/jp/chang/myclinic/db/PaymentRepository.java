package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.PaymentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    List<Payment> findByVisitIdOrderByPaytimeDesc(int visitIdt);

    Page<Payment> findAll(Pageable pageable);

    @Query("select payment, visit, patient from Payment payment, Visit visit, Patient patient " +
        " where payment.visitId = visit.visitId and patient.patientId = visit.patientId ")
    Page<Object[]> findAllFull(Pageable pageable);
}