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

    public void logPharmaQueueCreated(PharmaQueueDTO created){
        logValue("pharma-queue-created", new PharmaQueueCreated(created));
    }

    public void logPharmaQueueUpdated(PharmaQueueDTO prev, PharmaQueueDTO updated){
        logValue("pharma-queue-updated", new PharmaQueueUpdated(prev, updated));
    }

    public void logPharmaQueueDeleted(PharmaQueueDTO deleted){
        logValue("pharma-queue-deleted", new PharmaQueueDeleted(deleted));
    }

    public void logPatientCreated(PatientDTO created){
        logValue("patient-created", new PatientCreated(created));
    }

    public void logPatientUpdated(PatientDTO prev, PatientDTO updated){
        logValue("patient-updated", new PatientUpdated(prev, updated));
    }

    public void logPatientDeleted(PatientDTO deleted){
        logValue("patient-deleted", new PatientDeleted(deleted));
    }

    public void logShahokokuhoCreated(ShahokokuhoDTO created){
        logValue("shahokokuho-created", new ShahokokuhoCreated(created));
    }

    public void logShahokokuhoUpdated(ShahokokuhoDTO prev, ShahokokuhoDTO updated){
        logValue("shahokokuho-updated", new ShahokokuhoUpdated(prev, updated));
    }

    public void logShahokokuhoDeleted(ShahokokuhoDTO deleted){
        logValue("shahokokuho-deleted", new ShahokokuhoDeleted(deleted));
    }

    public void logKoukikoureiCreated(KoukikoureiDTO created){
        logValue("koukikourei-created", new KoukikoureiCreated(created));
    }

    public void logKoukikoureiUpdated(KoukikoureiDTO prev, KoukikoureiDTO updated){
        logValue("koukikourei-updated", new KoukikoureiUpdated(prev, updated));
    }

    public void logKoukikoureiDeleted(KoukikoureiDTO deleted){
        logValue("koukikourei-deleted", new KoukikoureiDeleted(deleted));
    }

    public void logRoujinCreated(RoujinDTO created){
        logValue("roujin-created", new RoujinCreated(created));
    }

    public void logRoujinUpdated(RoujinDTO prev, RoujinDTO updated){
        logValue("roujin-updated", new RoujinUpdated(prev, updated));
    }

    public void logRoujinDeleted(RoujinDTO deleted){
        logValue("roujin-deleted", new RoujinDeleted(deleted));
    }

    public void logKouhiCreated(KouhiDTO created){
        logValue("kouhi-created", new KouhiCreated(created));
    }

    public void logKouhiUpdated(KouhiDTO prev, KouhiDTO updated){
        logValue("kouhi-updated", new KouhiUpdated(prev, updated));
    }

    public void logKouhiDeleted(KouhiDTO deleted){
        logValue("kouhi-deleted", new KouhiDeleted(deleted));
    }

    public void logChargeCreated(ChargeDTO created){
        logValue("charge-created", new ChargeCreated(created));
    }

    public void logChargeUpdated(ChargeDTO prev, ChargeDTO updated){
        logValue("charge-updated", new ChargeUpdated(prev, updated));
    }

    public void logChargeDeleted(ChargeDTO deleted){
        logValue("charge-deleted", new ChargeDeleted(deleted));
    }

    public void logPaymentCreated(PaymentDTO created){
        logValue("payment-created", new PaymentCreated(created));
    }

    public void logPaymentUpdated(PaymentDTO prev, PaymentDTO updated){
        logValue("payment-updated", new PaymentUpdated(prev, updated));
    }

    public void logPaymentDeleted(PaymentDTO deleted){
        logValue("payment-deleted", new PaymentDeleted(deleted));
    }

    public void logShinryouCreated(ShinryouDTO created){
        logValue("shinryou-created", new ShinryouCreated(created));
    }

    public void logShinryouUpdated(ShinryouDTO prev, ShinryouDTO updated){
        logValue("shinryou-updated", new ShinryouUpdated(prev, updated));
    }

    public void logShinryouDeleted(ShinryouDTO deleted){
        logValue("shinryou-deleted", new ShinryouDeleted(deleted));
    }

    public void logDrugCreated(DrugDTO created){
        logValue("drug-created", new DrugCreated(created));
    }

    public void logDrugUpdated(DrugDTO prev, DrugDTO updated){
        logValue("drug-updated", new DrugUpdated(prev, updated));
    }

    public void logDrugDeleted(DrugDTO deleted){
        logValue("drug-deleted", new DrugDeleted(deleted));
    }

    public void logGazouLabelCreated(GazouLabelDTO created){
        logValue("gazou-label-created", new GazouLabelCreated(created));
    }

    public void logGazouLabelUpdated(GazouLabelDTO prev, GazouLabelDTO updated){
        logValue("gazou-label-updated", new GazouLabelUpdated(prev, updated));
    }

    public void logGazouLabelDeleted(GazouLabelDTO deleted){
        logValue("gazou-label-deleted", new GazouLabelDeleted(deleted));
    }

}
