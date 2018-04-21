package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ConductShinryouDTO;
import jp.chang.myclinic.dto.ConductShinryouFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
class ConductShinryouController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/enter-conduct-shinryou", method= RequestMethod.POST)
    public int enterConductShinryou(@RequestBody ConductShinryouDTO conductShinryou){
        return dbGateway.enterConductShinryou(conductShinryou);
    }

    @RequestMapping(value="/get-conduct-shinryou-full", method=RequestMethod.GET)
    public ConductShinryouFullDTO getConductShinryouFull(@RequestParam("conduct-shinryou-id") int conductShinryouId){
        return dbGateway.getConductShinryouFull(conductShinryouId);
    }

    @RequestMapping(value="/deleteById-conduct-shinryou", method=RequestMethod.POST)
    public boolean deleteConductShinryou(@RequestParam("conduct-shinryou-id") int conductShinryouId){
        dbGateway.deleteConductShinryou(conductShinryouId);
        return true;
    }

}
