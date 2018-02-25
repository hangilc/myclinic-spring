package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.TextVisitPageDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/json")
@Transactional
class TextController {
    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/update-text", method= RequestMethod.POST)
    public boolean updateText(@RequestBody TextDTO textDTO){
        dbGateway.updateText(textDTO);
        return true;
    }

    @RequestMapping(value="/enter-text", method= RequestMethod.POST)
    public int enterText(@RequestBody TextDTO textDTO){
        return dbGateway.enterText(textDTO);
    }

    @RequestMapping(value="/delete-text", method= RequestMethod.POST)
    public boolean enterText(@RequestParam("text-id") int textId){
        dbGateway.deleteText(textId);
        return true;
    }

    @RequestMapping(value="/get-text", method=RequestMethod.GET)
    public TextDTO getText(@RequestParam("text-id") int textId){
        return dbGateway.getText(textId);
    }


    @RequestMapping(value="/search-text", method=RequestMethod.GET)
    public TextVisitPageDTO searchText(@RequestParam("patient-id") int patientId, @RequestParam("text") String text,
                                       @RequestParam("page") int page){
        return dbGateway.searchText(patientId, text, page);
    }
}
