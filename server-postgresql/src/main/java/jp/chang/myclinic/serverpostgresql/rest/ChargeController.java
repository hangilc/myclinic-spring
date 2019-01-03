package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.ChargeDTO;
import jp.chang.myclinic.dto.ChargeOptionalDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
public class ChargeController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-charge", method=RequestMethod.POST)
	public boolean enterCharge(@RequestBody ChargeDTO chargeDTO){
		dbGateway.enterCharge(chargeDTO);
		return true;
	}

	@RequestMapping(value="/get-charge", method=RequestMethod.GET)
	public ChargeDTO getCharge(@RequestParam("visit-id") int visitId){
		return dbGateway.getCharge(visitId);
	}

	@RequestMapping(value="/find-charge", method=RequestMethod.GET)
	public ChargeOptionalDTO findCharge(@RequestParam("visit-id") int visitId){
		ChargeOptionalDTO chargeOptionalDTO = new ChargeOptionalDTO();
		chargeOptionalDTO.charge = dbGateway.findCharge(visitId).orElse(null);
		return chargeOptionalDTO;
	}

	@RequestMapping(value="/modify-charge", method=RequestMethod.POST)
	public boolean modifyCharge(@RequestParam("visit-id") int visitId, @RequestParam("charge") int chargeValue){
		dbGateway.setChargeOfVisit(visitId, chargeValue);
		return true;
	}

}