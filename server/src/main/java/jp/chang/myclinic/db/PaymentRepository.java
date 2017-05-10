package jp.chang.myclinic.db;

import jp.chang.myclinic.dto.PaymentDTO;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    List<Payment> findByVisitIdOrderByPaytimeDesc(int visitIdt);

}