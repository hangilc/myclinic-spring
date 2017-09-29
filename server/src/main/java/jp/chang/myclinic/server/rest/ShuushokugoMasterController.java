package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
class ShuushokugoMasterController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/search-shuushokugo-master", method= RequestMethod.GET)
    public List<ShuushokugoMasterDTO> searchShuushokugoMaster(@RequestParam("text") String text){
        return dbGateway.searchShuushokugoMaster(text);
    }
}
