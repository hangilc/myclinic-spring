package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.logdto.practicelog.PracticeLog;
import jp.chang.myclinic.server.PracticeLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
public class PracticeLogController {

    private static Logger logger = LoggerFactory.getLogger(PracticeLogController.class);
    @Autowired
    private PracticeLogger practiceLogger;

    @RequestMapping(value="/list-all-practice-logs", method= RequestMethod.GET)
    public List<PracticeLog> listAll(){
        return practiceLogger.getLogs();
    }

}
