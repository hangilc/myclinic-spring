package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.TextVisitPageDTO;
import jp.chang.myclinic.dto.TextVisitPatientPageDTO;
import jp.chang.myclinic.server.PracticeLogger;
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

    @Autowired
    private PracticeLogger practiceLogger;

    @RequestMapping(value="/update-text", method= RequestMethod.POST)
    public boolean updateText(@RequestBody TextDTO textDTO){
        TextDTO prev = dbGateway.getText(textDTO.textId);
        dbGateway.updateText(textDTO);
        practiceLogger.logTextUpdated(prev, textDTO);
        return true;
    }

    @RequestMapping(value="/enter-text", method= RequestMethod.POST)
    public int enterText(@RequestBody TextDTO textDTO){
        int textId =  dbGateway.enterText(textDTO);
        textDTO.textId = textId;
        practiceLogger.logTextCreated(textDTO);
        return textId;
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


    @RequestMapping(value="/search-text-by-page", method=RequestMethod.GET)
    public TextVisitPageDTO searchTextByPage(@RequestParam("patient-id") int patientId, @RequestParam("text") String text,
                                       @RequestParam("page") int page){
        return dbGateway.searchText(patientId, text, page);
    }

    @RequestMapping(value="/search-text-globally", method=RequestMethod.GET)
    public TextVisitPatientPageDTO searchTextGlobally(@RequestParam("text") String text,
                                                      @RequestParam("page") int page){
        int itemsPerPage = 10;
        return dbGateway.searchTextGlobally(text, page, itemsPerPage);
    }
}
