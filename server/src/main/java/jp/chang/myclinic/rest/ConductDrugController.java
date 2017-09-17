package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ConductDrugDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
class ConductDrugController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/enter-conduct-drug", method= RequestMethod.POST)
    public int enterConductDrug(@RequestBody ConductDrugDTO drug){
        return dbGateway.enterConductDrug(drug);
    }

    @RequestMapping(value="/delete-conduct-drug", method=RequestMethod.POST)
    public boolean deleteConductDrug(@RequestParam("conduct-drug-id") int conductDrugId){
        dbGateway.deleteConductDrug(conductDrugId);
        return true;
    }

}
