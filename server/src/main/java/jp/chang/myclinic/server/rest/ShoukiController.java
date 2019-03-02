package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.ShoukiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
class ShoukiController {

    //private static Logger logger = LoggerFactory.getLogger(ShoukiController.class);
    @Autowired
    private DbGatewayInterface dbGateway;

    @RequestMapping(value="batchy-get-shouki", method=RequestMethod.GET)
    List<ShoukiDTO> batchGetShouki(@RequestParam(value="visit-ids", required=false) List<Integer> visitIds){
        if( visitIds == null || visitIds.size() == 0 ){
            return Collections.emptyList();
        }
        return dbGateway.batchGetShouki(visitIds);
    }

    @RequestMapping(value="find-shouki", method=RequestMethod.GET)
    Optional<ShoukiDTO> findShouki(@RequestParam("visit-id") int visitId){
        return dbGateway.findShouki(visitId);
    }

    @RequestMapping(value="enter-shouki", method=RequestMethod.POST)
    boolean enterShouki(@RequestBody ShoukiDTO shouki){
        dbGateway.enterShouki(shouki);
        return true;
    }

    @RequestMapping(value="update-shouki", method=RequestMethod.POST)
    boolean updateShouki(@RequestBody ShoukiDTO shouki){
        dbGateway.updateShouki(shouki);
        return true;
    }

    @RequestMapping(value="delete-shouki", method=RequestMethod.POST)
    boolean deleteShouki(@RequestParam("visit-id") int visitId){
        dbGateway.deleteShouki(visitId);
        return true;
    }

}
