package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class IyakuhinMasterController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private MasterMap masterMap;

    @RequestMapping(value="/search-iyakuhin-master-by-name", method= RequestMethod.GET)
    public List<IyakuhinMasterDTO> searchIyakuhinMasterByName(@RequestParam("text") String text,
                                                            @RequestParam(value="at", defaultValue = "") String atString){
        LocalDate at;
        if( atString.equals("") ){
            at = LocalDate.now();
        } else {
            at = LocalDate.parse(atString);
        }
        return dbGateway.searchIyakuhinByName(text, at);
    }

    @RequestMapping(value="/resolve-iyakuhin-master", method=RequestMethod.GET)
    public Optional<IyakuhinMasterDTO> resolveIyakuhinMaster(@RequestParam("iyakuhincode") int iyakuhincode,
                                                             @RequestParam("at") String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        LocalDate atDate = LocalDate.parse(at);
        iyakuhincode = masterMap.resolveIyakuhinCode(iyakuhincode, atDate);
        return dbGateway.findIyakuhinMaster(iyakuhincode, at);
    }
}
