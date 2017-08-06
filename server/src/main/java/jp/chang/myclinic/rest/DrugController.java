package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
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

	@RequestMapping(value = "/get-drug-full", method = RequestMethod.GET)
	public DrugFullDTO getDrugFull(@RequestParam("drug-id") int drugId) {
		return dbGateway.getDrugFull(drugId);
	}

	@RequestMapping(value = "/list-drug-full", method = RequestMethod.GET)
	public List<DrugFullDTO> listDrugFull(@RequestParam("visit-id") int visitId) {
		return dbGateway.listDrugFull(visitId);
	}

	@RequestMapping(value = "/list-iyakuhin-for-patient", method = RequestMethod.GET)
	public List<IyakuhincodeNameDTO> listIyakuhinForPatient(@RequestParam("patient-id") int patientId) {
		return dbGateway.listIyakuhinForPatient(patientId);
	}

	@RequestMapping(value = "/list-visit-id-visited-at-by-patient-and-iyakuhincode", method = RequestMethod.GET)
	public List<VisitIdVisitedAtDTO> listVisitIdVisitedAtByPatientAndIyakuhincode(@RequestParam("patient-id") int patientId,
																				  @RequestParam("iyakuhincode") int iyakuhincode) {
		return dbGateway.listVisitIdVisitedAtByIyakuhincodeAndPatientId(patientId, iyakuhincode);
	}

	@RequestMapping(value = "/mark-drug-as-prescribed-for-visit", method = RequestMethod.POST)
	public boolean markDrugAsPrescribedForVisit(@RequestParam("visit-id") int visitId){
		dbGateway.markDrugsAsPrescribedForVisit(visitId);
		return true;
	}

	@RequestMapping(value="/presc-done", method=RequestMethod.POST)
	public boolean prescDone(@RequestParam("visit-id") int visitId){
		dbGateway.markDrugsAsPrescribedForVisit(visitId);
		dbGateway.findPharmaQueue(visitId)
				.ifPresent(pharmaQueueDTO1 -> dbGateway.deletePharmaQueue(pharmaQueueDTO1));
		dbGateway.findWqueue(visitId)
				.ifPresent(wqueueDTO1 -> dbGateway.deleteWqueue(wqueueDTO1));
		return true;
	}

	@RequestMapping(value="/search-prev-drug", method=RequestMethod.GET)
	public List<DrugFullDTO> searchPrevDrug(@RequestParam("patient-id") int patientId,
											@RequestParam(value="text", defaultValue="") String text){
		List<DrugFullDTO> result;
		if( text.isEmpty() ) {
			result = dbGateway.searchPrevDrug(patientId);
		} else {
			result = dbGateway.searchPrevDrug(patientId, text);
		}
		result.sort((o1, o2) -> {
            int i1 = o1.drug.visitId;
            int i2 = o2.drug.visitId;
            if( i1 < i2 ){
                return 1;
            } else if( i1 > i2 ){
                return -1;
            } else {
                int k1 = o1.drug.drugId;
                int k2 = o2.drug.drugId;
                return k1 - k2;
            }
        });
		return result;
	}
}