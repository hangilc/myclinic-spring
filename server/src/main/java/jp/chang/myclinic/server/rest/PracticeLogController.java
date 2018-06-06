package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.TodaysVisitsWithLogInfoDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLogList;
import jp.chang.myclinic.server.PracticeLogger;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/json")
public class PracticeLogController {

    //private static Logger logger = LoggerFactory.getLogger(PracticeLogController.class);
    @Autowired
    private PracticeLogger practiceLogger;
    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value = "/list-all-practice-log", method = RequestMethod.GET)
    public PracticeLogList listAll() {
        PracticeLogList result = new PracticeLogList();
        result.serverId = practiceLogger.getServerId();
        result.logs = practiceLogger.getLogs();
        return result;
    }

    @RequestMapping(value = "/list-practice-log-after", method = RequestMethod.GET)
    public PracticeLogList listLogsAfter(@RequestParam("serial-id") int serialId){
        PracticeLogList result = new PracticeLogList();
        result.serverId = practiceLogger.getServerId();
        result.logs = practiceLogger.listLogsAfter(serialId);
        return result;
    }

    @RequestMapping(value = "list-todays-visits-with-log-info", method = RequestMethod.GET)
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public TodaysVisitsWithLogInfoDTO listTodaysVisitsWithLogInfo(){
        TodaysVisitsWithLogInfoDTO result = new TodaysVisitsWithLogInfoDTO();
        result.serverId = practiceLogger.getServerId();
        result.serialId = practiceLogger.getSerialId();
        result.visits = dbGateway.listVisitFull2PatientOfToday();
        return result;
    }

}
