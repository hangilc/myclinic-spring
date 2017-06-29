package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.dto.VisitIdVisitedAtDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class DrugController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-drug-full", method=RequestMethod.GET)
	public DrugFullDTO getDrugFull(@RequestParam("drug-id") int drugId){
		return dbGateway.getDrugFull(drugId);
	}

	@RequestMapping(value="/list-drug-full", method=RequestMethod.GET)
	public List<DrugFullDTO> listDrugFull(@RequestParam("visit-id") int visitId){
		return dbGateway.listDrugFull(visitId);
	}

	@RequestMapping(value="/list-iyakuhin-for-patient", method=RequestMethod.GET)
	public List<IyakuhincodeNameDTO> listIyakuhinForPatient(@RequestParam("patient-id") int patientId){
		return dbGateway.listIyakuhinForPatient(patientId);
	}

	@RequestMapping(value="list-visit-id-visited-at-by-patient-and-iyakuhincode", method=RequestMethod.GET)
	public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByPatientAndIyakuhincode(@RequestParam("patient-id") int patientId,
																			   @RequestParam("iyakuhincode") int iyakuhincode){
		return dbGateway.listVisitIdVisitedAtByIyakuhincodeAndPatientId(patientId, iyakuhincode);
	}
}