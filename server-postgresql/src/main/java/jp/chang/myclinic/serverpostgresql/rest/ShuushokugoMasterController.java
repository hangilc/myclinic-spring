package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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

    @RequestMapping(value="/find-shuushokugo-master-by-name", method=RequestMethod.GET)
    public Optional<ShuushokugoMasterDTO> findByName(@RequestParam("name") String name){
        return dbGateway.findShuushokugoMasterByName(name);
    }
}
