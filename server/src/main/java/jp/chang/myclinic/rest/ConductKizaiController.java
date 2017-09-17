package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ConductKizaiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
