package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.HokenDTO;
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
}
