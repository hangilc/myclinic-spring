package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class ShinryouAttrController {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouAttrController.class);

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="batch-get-shinryou-attr", method=RequestMethod.GET)
    public List<ShinryouAttrDTO> batchGetShinryouAttr(
            @RequestParam(value="shinryou-ids", required=false) List<Integer> shinryouIds){
        if( shinryouIds == null ){
            return Collections.emptyList();
        }
        return dbGateway.batchGetShinryouAttr(shinryouIds);
    }

    @RequestMapping(value="find-shinryou-attr", method=RequestMethod.GET)
    public Optional<ShinryouAttrDTO> findShinryouAttr(@RequestParam("shinryou-id") int shinryouId){
        return dbGateway.findShinryouAttr(shinryouId);
    }

    @RequestMapping(value="set-shinryou-tekiyou", method=RequestMethod.POST)
    public ShinryouAttrDTO setShinryouTekiyou(@RequestParam("shinryou-id") int shinryouId,
                                              @RequestParam("tekiyou") String tekiyou){
        return dbGateway.setShinryouTekiyou(shinryouId, tekiyou);
    }

    @RequestMapping(value="delete-shinryou-tekiyou", method=RequestMethod.POST)
    public Optional<ShinryouAttrDTO> deleteShinryouTekiyou(@RequestParam("shinryou-id") int shinryouId){
        return dbGateway.deleteShinryouTekiyou(shinryouId);
    }

    @RequestMapping(value="enter-shinryou-attr", method=RequestMethod.POST)
    public boolean enterShinryouAttr(@RequestBody() ShinryouAttrDTO shinryouAttr){
        dbGateway.enterShinryouAttr(shinryouAttr);
        return true;
    }

}
