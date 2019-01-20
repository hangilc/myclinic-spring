package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.KizaiMasterDTO;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.serverpostgresql.MasterMapUtil;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
class KizaiMasterController {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouMasterController.class);

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private MasterMapUtil masterMapUtil;

    @RequestMapping(value = "/search-kizai-master-by-name", method = RequestMethod.GET)
    List<KizaiMasterDTO> searchByName(@RequestParam("text") String text, @RequestParam("at") String at) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        LocalDate date = LocalDate.parse(at);
        return dbGateway.searchKizaiMasterByName(text, date);
    }

    @RequestMapping(value = "/resolve-kizai-master-by-name", method = RequestMethod.GET)
    public KizaiMasterDTO resolveKizaiMasterByName(@RequestParam("name") String name,
                                                   @RequestParam("at") String at) {
        LocalDate atDate = convertToDate(at);
        return masterMapUtil.resolveKizaiMaster(name, atDate)
                .orElseThrow(() -> new RuntimeException("器材マスターを見つけられませんでした。[" + name + "]"));
    }

    @RequestMapping(value = "/resolve-kizai-master", method = RequestMethod.GET)
    public KizaiMasterDTO resolveKizaiMaster(@RequestParam("kizaicode") int kizaicode,
                                             @RequestParam("at") String at) {
        LocalDate atDate = convertToDate(at);
        int newKizaicode = masterMapUtil.adaptKizaicode(kizaicode, atDate);
        return dbGateway.findKizaiMasterByKizaicode(newKizaicode, atDate)
                .orElseThrow(() ->
                        new RuntimeException("器材マスターを見つけられませんでした。[" + kizaicode + "]"));
    }

    private LocalDate convertToDate(String at) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        return LocalDate.parse(at);
    }

}
