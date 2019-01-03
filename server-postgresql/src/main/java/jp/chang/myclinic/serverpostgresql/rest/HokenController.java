package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.HokenListDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
public class HokenController {
    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/list-available-hoken", method= RequestMethod.GET)
    public HokenDTO listAvailableHoken(@RequestParam("patient-id") int patientId,
                                       @RequestParam("at") String at){
        return dbGateway.listAvailableHoken(patientId, at);
    }

    @RequestMapping(value="/convert-to-hoken", method=RequestMethod.POST)
    public HokenDTO convertToHoken(@RequestBody VisitDTO visitDTO){
        HokenDTO hoken = new HokenDTO();
        if( visitDTO.shahokokuhoId != 0 ){
            hoken.shahokokuho = dbGateway.getShahokokuho(visitDTO.shahokokuhoId);
        }
        if( visitDTO.koukikoureiId != 0 ){
            hoken.koukikourei = dbGateway.getKoukikourei(visitDTO.koukikoureiId);
        }
        if( visitDTO.roujinId != 0 ){
            hoken.roujin = dbGateway.getRoujin(visitDTO.roujinId);
        }
        if( visitDTO.kouhi1Id != 0 ){
            hoken.kouhi1 = dbGateway.getKouhi(visitDTO.kouhi1Id);
        }
        if( visitDTO.kouhi2Id != 0 ){
            hoken.kouhi2 = dbGateway.getKouhi(visitDTO.kouhi2Id);
        }
        if( visitDTO.kouhi3Id != 0 ){
            hoken.kouhi3 = dbGateway.getKouhi(visitDTO.kouhi3Id);
        }
        return hoken;
    }

	@RequestMapping(value="/list-hoken", method=RequestMethod.GET)
	public HokenListDTO listHoken(@RequestParam(name="patient-id") int patientId){
		return dbGateway.findHokenByPatient(patientId);
	}

}
