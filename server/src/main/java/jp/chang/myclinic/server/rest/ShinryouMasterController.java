package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
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
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class ShinryouMasterController {

    private static Logger logger = LoggerFactory.getLogger(ShinryouMasterController.class);

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private MasterMap masterMap;

    @RequestMapping(value="/resolve-shinryoucode", method=RequestMethod.GET)
    public int resolveShinryoucode(@RequestParam("shinryoucode") int shinryoucode,
                                   @RequestParam("at") String at){
        LocalDate atDate = convertToDate(at);
        return masterMap.resolveShinryouCode(shinryoucode, atDate);
    }

    @RequestMapping(value="/resolve-shinryou-master-by-name", method=RequestMethod.GET)
    public ShinryouMasterDTO resolveShinryouMasterByName(@RequestParam("name") String name,
                                                         @RequestParam("at") String at){
        LocalDate atDate = convertToDate(at);
        try {
            int shinryoucode = masterMap.getShinryoucodeByName(name).orElseThrow(() -> new RuntimeException("cannot find name"));
            shinryoucode = masterMap.resolveShinryouCode(shinryoucode, atDate);
            return dbGateway.getShinryouMaster(shinryoucode, atDate);
        } catch(Exception ex){
            logger.error("Failed to resolve shinryou master by name. {}", name, ex);
            throw new RuntimeException("診療行為マスターを見つけられませんでした。[" + name + "]: " + ex);
        }
    }

    @RequestMapping(value="/get-shinryou-master", method=RequestMethod.GET)
    public ShinryouMasterDTO getShinryouMaster(@RequestParam("shinryoucode") int shinryoucode,
                                               @RequestParam("at") String at){
        LocalDate atDate = convertToDate(at);
        return dbGateway.getShinryouMaster(shinryoucode, atDate);
    }

    @RequestMapping(value="find-shinryou-master-by-name", method=RequestMethod.GET)
    public Optional<ShinryouMasterDTO> findShinryouMasterByName(@RequestParam("name") String name,
                                                                @RequestParam("at") String at){
        LocalDate atDate = convertToDate(at);
        return dbGateway.findShinryouMasterByName(name, atDate);
    }

    private LocalDate convertToDate(String at){
        if( at.length() > 10 ){
            at = at.substring(0, 10);
        }
        return LocalDate.parse(at);
    }
}
