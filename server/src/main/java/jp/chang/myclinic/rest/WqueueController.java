package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.WqueueFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class WqueueController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/list-wqueue-full", method=RequestMethod.GET)
	public List<WqueueFullDTO> listWqueueFull(){
		return dbGateway.listWqueueFull();
	}

//	@RequestMapping(value="/list-wqueue-full-for-prescription", method=RequestMethod.GET)
//	public List<WqueueFullDTO> listWqueueFullForPrescription(){
//		return dbGateway.listWqueueFullByStates(EnumSet.of(WqueueWaitState.WaitCashier, WqueueWaitState.WaitDrug))
//	}
}