package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
@Transactional
class GazouLabelController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/modify-gazou-label", method= RequestMethod.POST)
    public boolean modifyGazouLabel(@RequestParam("conduct-id") int conductId, @RequestParam("label") String label){
        dbGateway.modifyGazouLabel(conductId, label);
        return true;
    }
}
