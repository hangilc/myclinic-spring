package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.dto.PaymentVisitPatientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class PaymentController {

	@Autowired
	private DbGatewayInterface dbGateway;

	@RequestMapping(value="/enter-payment", method=RequestMethod.POST)
	public boolean enterPayment(@RequestBody PaymentDTO paymentDTO){
		dbGateway.enterPayment(paymentDTO);
		return true;
	}

	@RequestMapping(value="/list-payment", method=RequestMethod.GET)
	public List<PaymentDTO> listPayment(@RequestParam("visit-id") int visitId){
		return dbGateway.listPayment(visitId);
	}

	@RequestMapping(value = "/list-recent-payment", method = RequestMethod.GET)
	public List<PaymentVisitPatientDTO> listRecentPayment(@RequestParam(value = "n", defaultValue = "30") int n){
		return dbGateway.listRecentPayment(n);
	}

	@RequestMapping(value = "/list-payment-by-patient", method = RequestMethod.GET)
	public List<PaymentVisitPatientDTO> listPaymentByPatient(@RequestParam(value="patient-id") int patientId,
															 @RequestParam(value = "n", defaultValue = "30") int n){
		return dbGateway.listPaymentByPatient(patientId, n);
	}

	@RequestMapping(value = "/list-final-payment", method = RequestMethod.GET)
	public List<PaymentDTO> listFinalPayment(@RequestParam(value = "n", defaultValue = "30") int n){
		return dbGateway.listFinalPayment(n);
	}

}