package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.KizaiMasterDTO;
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

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/search-kizai-master-by-name", method= RequestMethod.GET)
    List<KizaiMasterDTO> searchByName(@RequestParam("text") String text, @RequestParam("at") String at){
        LocalDate date = LocalDate.parse(at);
        return dbGateway.searchKizaiMasterByName(text, date);
    }

}
