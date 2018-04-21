package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.ConductKizaiDTO;
import jp.chang.myclinic.dto.ConductKizaiFullDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
class ConductKizaiController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/enter-conduct-kizai", method= RequestMethod.POST)
    public int enterConductKizai(@RequestBody ConductKizaiDTO kizai){
        return dbGateway.enterConductKizai(kizai);
    }

    @RequestMapping(value="/deleteById-conduct-kizai", method=RequestMethod.POST)
    public boolean deleteConductKizai(@RequestParam("conduct-kizai-id") int conductKizaiId){
        dbGateway.deleteConductKizai(conductKizaiId);
        return true;
    }

    @RequestMapping(value="/get-conduct-kizai-full")
    public ConductKizaiFullDTO getConductKizaiFull(@RequestParam("conduct-kizai-id") int conductKizaiId){
        return dbGateway.getConductKizaiFull(conductKizaiId);
    }

}
