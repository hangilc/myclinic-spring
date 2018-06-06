package jp.chang.myclinic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.*;
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

    public void logVisitDeleted(VisitDTO deleted){
        logValue("visit-deleted", new VisitDeleted(deleted));
    }

    public void logVisitUpdated(VisitDTO prev, VisitDTO updated){
        logValue("hoken-updated", new VisitUpdated(prev, updated));
    }

    public void logWqueueCreated(WqueueDTO wqueue){
        logValue("wqueue-created", new WqueueCreated(wqueue));
    }

    public void logWqueueDeleted(WqueueDTO deleted){
        logValue("wqueue-deleted", new WqueueDeleted(deleted));
    }

    public void logWqueueUpdated(WqueueDTO prev, WqueueDTO updated){
        logValue("wqueue-updated", new WqueueUpdated(prev, updated));
    }

    public void logTextUpdated(TextDTO prev, TextDTO updated){
        logValue("text-updated", new TextUpdated(prev, updated));
    }

    public void logTextCreated(TextDTO text){
        logValue("text-created", new TextCreated(text));
    }

    public void logTextDeleted(TextDTO deleted){
        logValue("text-deleted", new TextDeleted(deleted));
    }

    public void logDrugUpdated(DrugDTO prev,DrugDTO updated){
        logValue("drug-updated", new DrugUpdated(prev, updated));
    }

    public void logPharmaQueueCreated(PharmaQueueDTO created){
        logValue("pharma-queue-created", new PharmaQueueCreated(created));
    }

    public void logPharmaQueueUpdated(PharmaQueueDTO prev, PharmaQueueDTO updated){
        logValue("pharma-queue-updated", new PharmaQueueUpdated(prev, updated));
    }

    public void logPharmaQueueDeleted(PharmaQueueDTO deleted){
        logValue("pharma-queue-deleted", new PharmaQueueCreated(deleted));
    }

    public void logPatientCreated(PatientDTO created){
        logValue("patient-created", new PatientCreated(created));
    }

    public void logPatientUpdated(PatientDTO prev, PatientDTO updated){
        logValue("patient-updated", new PatientUpdated(prev, updated));
    }

    public void logPatientDeleted(PatientDTO deleted){
        logValue("patient-deleted", new PatientCreated(deleted));
    }

}
