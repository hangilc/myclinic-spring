package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value="/get-hoken", method=RequestMethod.GET)
    public HokenDTO getHoken(@RequestParam("visit-id") int visitId){
        VisitDTO visit = dbGateway.getVisit(visitId);
        return dbGateway.getHokenForVisit(visit);
    }
}
