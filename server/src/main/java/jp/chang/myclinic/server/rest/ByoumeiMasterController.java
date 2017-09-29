package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
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
class ByoumeiMasterController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/search-byoumei-master", method= RequestMethod.GET)
    public List<ByoumeiMasterDTO> searchByoumeiMaster(@RequestParam("text") String text,
                                                      @RequestParam("at") String at){
        LocalDate atDate = LocalDate.parse(at);
        return dbGateway.searchByoumeiMaster(text, atDate);
    }
}
