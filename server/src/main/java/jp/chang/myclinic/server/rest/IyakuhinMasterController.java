package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/json")
@Transactional
public class IyakuhinMasterController {

    private static Logger logger = LoggerFactory.getLogger(IyakuhinMasterController.class);

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
            if( atString.length() > 10 ){
                atString = atString.substring(0, 10);
            }
            at = LocalDate.parse(atString);
        }
        return dbGateway.searchIyakuhinByName(text, at);
    }

    @RequestMapping(value="/resolve-iyakuhin-master", method=RequestMethod.GET)
    public Optional<IyakuhinMasterDTO> resolveIyakuhinMaster(@RequestParam("iyakuhincode") int iyakuhincode,
                                                             @RequestParam("at") String at){
        LocalDate atDate = convertToDate(at);
        iyakuhincode = masterMap.resolveIyakuhinCode(iyakuhincode, atDate);
        return dbGateway.findIyakuhinMaster(iyakuhincode, at);
    }

    @RequestMapping(value="/batch-resolve-iyakuhin-master", method=RequestMethod.GET)
    public Map<Integer, IyakuhinMasterDTO> batchResolveIyakuhinMaster(@RequestParam(value = "iyakuhincode", required = false) List<Integer> iyakuhincodes,
                                                                      @RequestParam("at") String at){
        if( iyakuhincodes == null ){
            iyakuhincodes = Collections.emptyList();
        }
        Map<Integer, IyakuhinMasterDTO> map = new HashMap<>();
        LocalDate atDate = LocalDate.parse(at);
        iyakuhincodes.forEach(iyakuhincode -> {
            int resolvedIyakuhincode = masterMap.resolveIyakuhinCode(iyakuhincode, atDate);
            Optional<IyakuhinMasterDTO> optMaster = dbGateway.findIyakuhinMaster(resolvedIyakuhincode, at);
            map.put(iyakuhincode, optMaster.orElse(null));
        });
        return map;
    }

    @RequestMapping(value="/get-name-of-iyakuhin", method=RequestMethod.GET)
    public Optional<String> getNameOfIyakuhin(@RequestParam(value="iyakuhincode") int iyakuhincode){
        return dbGateway.findNameForIyakuhincode(iyakuhincode);
    }

    private LocalDate convertToDate(String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        return LocalDate.parse(at);
    }

}
