package jp.chang.myclinic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.logdto.practicelog.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PracticeLogger {

    //private static Logger logger = LoggerFactory.getLogger(PracticeLogger.class);
    private static ObjectMapper mapper;
    private String server = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    private AtomicInteger serialId = new AtomicInteger();
    private List<PracticeLog> logs = new ArrayList<>();

    PracticeLogger() {
        mapper = new ObjectMapper();
    }

    private void logValue(String kind, PracticeLogBody obj){
        try {
            String body = mapper.writeValueAsString(obj);
            PracticeLog practiceLog = new PracticeLog(server, serialId.incrementAndGet(), kind, body);
            logs.add(practiceLog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to log practice.", e);
        }
    }

    public List<PracticeLog> getLogs(){
        return logs;
    }

    public void logVisitCreated(VisitDTO visit){
        logValue("visit-created", new VisitCreated(visit));
    }

    public void logVisitDeleted(int visitId){
        logValue("visit-deleted", new VisitDeleted(visitId));
    }

    public void logWqueueCreated(WqueueDTO wqueue){
        logValue("wqueue-created", new WqueueCreated(wqueue));
    }

    public void logHokenUpdated(VisitDTO prev, VisitDTO updated){
        logValue("hoken-updated", new HokenUpdated(prev, updated));
    }

    public void logTextUpdated(TextDTO prev, TextDTO updated){
        logValue("text-updated", new TextUpdated(prev, updated));
    }

    public void logTextCreated(TextDTO text){
        logValue("text-created", new TextCreated(text));
    }
}
