package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/json")
@Transactional
public class ShinryouMasterController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private MasterMap masterMap;

    @RequestMapping(value="/resolve-shinryoucode", method=RequestMethod.GET)
    public int resolveShinryoucode(@RequestParam("shinryoucode") int shinryoucode,
                                   @RequestParam("at") String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        LocalDate atDate = LocalDate.parse(at);
        return masterMap.resolveShinryouCode(shinryoucode, atDate);
    }

    @RequestMapping(value="/get-shinryou-master", method=RequestMethod.GET)
    public ShinryouMasterDTO getShinryouMaster(@RequestParam("shinryoucode") int shinryoucode,
                                               @RequestParam("at") String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        LocalDate atDate = LocalDate.parse(at);
        return dbGateway.getShinryouMaster(shinryoucode, atDate);
    }
}
