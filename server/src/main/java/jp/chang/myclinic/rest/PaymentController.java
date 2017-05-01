package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
@Transactional
public class PaymentController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-payment", method=RequestMethod.POST)
	public boolean enterPayment(@RequestBody PaymentDTO paymentDTO){
		dbGateway.enterPayment(paymentDTO);
		return true;
	}

}