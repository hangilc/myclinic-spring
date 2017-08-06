package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
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
class PrescExampleController {
    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/search-presc-example-full-by-name", method= RequestMethod.GET)
    public List<PrescExampleFullDTO> searchByName(@RequestParam("text") String text){
        return dbGateway.searchPrescExampleFullByName(text);
    }
}
