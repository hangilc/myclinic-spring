package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.DrugAttrDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
class DrugAttrController {

    //private static Logger logger = LoggerFactory.getLogger(DrugAttrController.class);

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="batch-get-drug-attr", method=RequestMethod.GET)
    public List<DrugAttrDTO> batchGetDrugAttr(
            @RequestParam(value="drug-ids", required=false) List<Integer> drugIds){
        if( drugIds == null || drugIds.size() == 0 ){
            return Collections.emptyList();
        }
        return dbGateway.batchGetDrugAttr(drugIds);
    }

    @RequestMapping(value="find-drug-attr", method=RequestMethod.GET)
    public Optional<DrugAttrDTO> findDrugAttr(@RequestParam("drug-id") int drugId){
        return dbGateway.findDrugAttr(drugId);
    }

    @RequestMapping(value="set-drug-tekiyou", method=RequestMethod.POST)
    public DrugAttrDTO setDrugTekiyou(@RequestParam("drug-id") int drugId,
                                              @RequestParam("tekiyou") String tekiyou){
        return dbGateway.setDrugTekiyou(drugId, tekiyou);
    }

    @RequestMapping(value="delete-drug-tekiyou", method=RequestMethod.POST)
    public Optional<DrugAttrDTO> deleteDrugTekiyou(@RequestParam("drug-id") int drugId){
        return dbGateway.deleteDrugTekiyou(drugId);
    }

    @RequestMapping(value="enter-drug-attr", method=RequestMethod.POST)
    public boolean enterDrugAttr(@RequestBody() DrugAttrDTO drugAttr){
        dbGateway.enterDrugAttr(drugAttr);
        return true;
    }

}
