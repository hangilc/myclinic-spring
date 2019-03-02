package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.ConductDrugDTO;
import jp.chang.myclinic.dto.ConductDrugFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
class ConductDrugController {

    @Autowired
    private DbGatewayInterface dbGateway;

    @RequestMapping(value="/enter-conduct-drug", method= RequestMethod.POST)
    public int enterConductDrug(@RequestBody ConductDrugDTO drug){
        return dbGateway.enterConductDrug(drug);
    }

    @RequestMapping(value="/delete-conduct-drug", method=RequestMethod.POST)
    public boolean deleteConductDrug(@RequestParam("conduct-drug-id") int conductDrugId){
        dbGateway.deleteConductDrug(conductDrugId);
        return true;
    }

    @RequestMapping(value="/get-conduct-drug-full", method=RequestMethod.GET)
    public ConductDrugFullDTO getConductDrugFull(@RequestParam("conduct-drug-id") int conductDrugId){
        return dbGateway.getConductDrugFull(conductDrugId);
    }

}
