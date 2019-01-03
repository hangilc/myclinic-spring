package jp.chang.myclinic.serverpostgresql.rest;

import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import jp.chang.myclinic.serverpostgresql.PracticeLogger;
import jp.chang.myclinic.serverpostgresql.db.myclinic.DbGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/json")
public class PracticeLogController {

    @Autowired
    private DbGateway dbGateway;
    @Autowired
    private PracticeLogger practiceLogger;

    @RequestMapping(value = "/list-all-practice-log", method = RequestMethod.GET)
    public List<PracticeLogDTO> listAll(@RequestParam("date") String date) {
        return dbGateway.listPracticeLogByDate(LocalDate.parse(date));
    }

    @RequestMapping(value = "/list-practice-log-after", method = RequestMethod.GET)
    public List<PracticeLogDTO> listLogAfter(@RequestParam("date") String date,
                                             @RequestParam("last-id") int lastId) {
        return dbGateway.listRecentPracticeLog(LocalDate.parse(date), lastId);
    }

    @RequestMapping(value = "/list-practice-log-in-range", method = RequestMethod.GET)
    public List<PracticeLogDTO> listLogInRange(@RequestParam("date") String date,
                                               @RequestParam("after-id") int afterId,
                                               @RequestParam("before-id") int beforeId) {
        return dbGateway.listPracticeLogInRange(LocalDate.parse(date), afterId, beforeId);
    }

    @RequestMapping(value = "/emit-wqueue-deleted-practice-log", method = RequestMethod.POST)
    public boolean emitWqueueDeleted(@RequestBody WqueueDTO dto){
        practiceLogger.logWqueueDeleted(dto);
        return true;
    }

}
