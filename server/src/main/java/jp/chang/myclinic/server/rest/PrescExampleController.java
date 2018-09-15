package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.PrescExampleDTO;
import jp.chang.myclinic.dto.PrescExampleFullDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/enter-presc-example", method=RequestMethod.POST)
    public int enterPrescExample(@RequestBody PrescExampleDTO example){
        if( dbGateway.findNameForIyakuhincode(example.iyakuhincode).isPresent() ){
            return dbGateway.enterPrescExample(example);
        } else {
            throw new RuntimeException("Invalid iyakuhincode: " + example.iyakuhincode);
        }
    }

    @RequestMapping(value="/update-presc-example", method=RequestMethod.POST)
    public boolean updatePrescExample(@RequestBody PrescExampleDTO example){
        PrescExampleDTO cur = dbGateway.findPrescExample(example.prescExampleId);
        if( cur == null ){
            throw new RuntimeException("Cannot find presc example with id " + example.prescExampleId);
        }
        dbGateway.updatePrescExample(example);
        return true;
    }

}
