package jp.chang.myclinic.tracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.logdto.practicelog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

class Dispatcher implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Semaphore taskPermit = new Semaphore(1);
    private BlockingQueue<PracticeLogDTO> pendings = new LinkedBlockingQueue<>();
    private DispatchAction action;
    private ListLogFunction listLogFun;
    private Runnable toNext;
    private int lastId;
    private Consumer<Runnable> callbackWrapper = Runnable::run;

    Dispatcher(DispatchAction action, ListLogFunction listLogFun) {
        this.action = action;
        this.listLogFun = listLogFun;
        toNext = () -> taskPermit.release();
    }

    void setCallbackWrapper(Consumer<Runnable> callbackWrapper){
        this.callbackWrapper = callbackWrapper;
    }

    void add(PracticeLogDTO plog) {
        try {
            pendings.put(plog);
        } catch (InterruptedException e) {
            logger.error("Failed to add practice log.", e);
        }
    }

    @Override
    public void run() {
        try {
            taskPermit.acquire();
            //noinspection InfiniteLoopStatement
            while (true) {
                PracticeLogDTO plog = pendings.take();
                int plogSerialId = plog.serialId;
                if (plogSerialId == (lastId + 1)) {
                    dispatchOne(plog);
                    taskPermit.acquire();
                    this.lastId = plog.serialId;
                } else if ( plogSerialId > (lastId + 1)) {
                    catchUp(lastId, plogSerialId, plog);
                }
            }
        } catch (InterruptedException ignored) {

        }
    }

    void beforeCatchup(){

    }

    void afterCatchup(){

    }

    private void catchUp(int lastId, int nextId, PracticeLogDTO nextLog) throws InterruptedException {
        String today = LocalDate.now().toString();
        boolean needCallback = false;
        try {
            final List<PracticeLogDTO> logs =
                    listLogFun.listPracticeLogInRangeCall(today, lastId, nextId).execute().body();
            logs.add(nextLog);
            if( logs.size() > 6 ){
                beforeCatchup();
                needCallback = true;
            }
            class Local {
                private int i = 0;
            }
            Local local = new Local();
            while( local.i < logs.size() ){
                PracticeLogDTO currentLog = logs.get(local.i);
                dispatchOne(currentLog);
                taskPermit.acquire();
                this.lastId = currentLog.serialId;
                local.i += 1;
            }
            if( needCallback ){
                afterCatchup();
            }
        } catch (IOException e) {
            if( needCallback ){
                afterCatchup();
            }
            logger.error("Client::listPracticeLogInRange failed.", e);
        }
    }

    private void dispatchOne(PracticeLogDTO log) {
        //System.err.println("DispatchOne: " + log);
        try {
            switch (log.kind) {
                case "visit-created": {
                    VisitCreated body = mapper.readValue(log.body, VisitCreated.class);
                    callbackWrapper.accept(() -> action.onVisitCreated(body.created, toNext));
                    break;
                }
                case "visit-deleted": {
                    VisitDeleted body = mapper.readValue(log.body, VisitDeleted.class);
                    callbackWrapper.accept(() -> action.onVisitDeleted(body.deleted, toNext));
                    break;
                }
                case "text-created": {
                    TextCreated body = mapper.readValue(log.body, TextCreated.class);
                    callbackWrapper.accept(() -> action.onTextCreated(body.created, toNext));
                    break;
                }
                case "text-updated": {
                    TextUpdated body = mapper.readValue(log.body, TextUpdated.class);
                    callbackWrapper.accept(() -> action.onTextUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "text-deleted": {
                    TextDeleted body = mapper.readValue(log.body, TextDeleted.class);
                    callbackWrapper.accept(() -> action.onTextDeleted(body.deleted, toNext));
                    break;
                }
                case "drug-created": {
                    DrugCreated body = mapper.readValue(log.body, DrugCreated.class);
                    callbackWrapper.accept(() -> action.onDrugCreated(body.created, toNext));
                    break;
                }
                case "drug-updated": {
                    DrugUpdated body = mapper.readValue(log.body, DrugUpdated.class);
                    callbackWrapper.accept(() -> action.onDrugUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "drug-deleted": {
                    DrugDeleted body = mapper.readValue(log.body, DrugDeleted.class);
                    callbackWrapper.accept(() -> action.onDrugDeleted(body.deleted, toNext));
                    break;
                }
                case "shinryou-created": {
                    ShinryouCreated body = mapper.readValue(log.body, ShinryouCreated.class);
                    callbackWrapper.accept(() -> action.onShinryouCreated(body.created, toNext));
                    break;
                }
                case "shinryou-deleted": {
                    ShinryouDeleted body = mapper.readValue(log.body, ShinryouDeleted.class);
                    callbackWrapper.accept(() -> action.onShinryouDeleted(body.deleted, toNext));
                    break;
                }
                case "conduct-created": {
                    ConductCreated body = mapper.readValue(log.body, ConductCreated.class);
                    callbackWrapper.accept(() -> action.onConductCreated(body.created, toNext));
                    break;
                }
                case "conduct-updated": {
                    ConductUpdated body = mapper.readValue(log.body, ConductUpdated.class);
                    callbackWrapper.accept(() -> action.onConductUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "gazou-label-created": {
                    GazouLabelCreated body = mapper.readValue(log.body, GazouLabelCreated.class);
                    callbackWrapper.accept(() -> action.onGazouLabelCreated(body.created, toNext));
                    break;
                }
                case "gazou-label-updated": {
                    GazouLabelUpdated body = mapper.readValue(log.body, GazouLabelUpdated.class);
                    callbackWrapper.accept(() -> action.onGazouLabelUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "conduct-shinryou-created": {
                    ConductShinryouCreated body = mapper.readValue(log.body, ConductShinryouCreated.class);
                    callbackWrapper.accept(() -> action.onConductShinryouCreated(body.created, toNext));
                    break;
                }
                case "conduct-shinryou-deleted": {
                    ConductShinryouDeleted body = mapper.readValue(log.body, ConductShinryouDeleted.class);
                    callbackWrapper.accept(() -> action.onConductShinryouDeleted(body.deleted, toNext));
                    break;
                }
                case "conduct-drug-created": {
                    ConductDrugCreated body = mapper.readValue(log.body, ConductDrugCreated.class);
                    callbackWrapper.accept(() -> action.onConductDrugCreated(body.created, toNext));
                    break;
                }
                case "conduct-drug-deleted": {
                    ConductDrugDeleted body = mapper.readValue(log.body, ConductDrugDeleted.class);
                    callbackWrapper.accept(() -> action.onConductDrugDeleted(body.deleted, toNext));
                    break;
                }
                case "conduct-kizai-created": {
                    ConductKizaiCreated body = mapper.readValue(log.body, ConductKizaiCreated.class);
                    callbackWrapper.accept(() -> action.onConductKizaiCreated(body.created, toNext));
                    break;
                }
                case "conduct-kizai-deleted": {
                    ConductKizaiDeleted body = mapper.readValue(log.body, ConductKizaiDeleted.class);
                    callbackWrapper.accept(() -> action.onConductKizaiDeleted(body.deleted, toNext));
                    break;
                }
                case "charge-created": {
                    ChargeCreated body = mapper.readValue(log.body, ChargeCreated.class);
                    callbackWrapper.accept(() -> action.onChargeCreated(body.created, toNext));
                    break;
                }
                case "charge-updated": {
                    ChargeUpdated body = mapper.readValue(log.body, ChargeUpdated.class);
                    callbackWrapper.accept(() -> action.onChargeUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "payment-created": {
                    PaymentCreated body = mapper.readValue(log.body, PaymentCreated.class);
                    callbackWrapper.accept(() -> action.onPaymentCreated(body.created, toNext));
                    break;
                }
                case "wqueue-created": {
                    WqueueCreated body = mapper.readValue(log.body, WqueueCreated.class);
                    callbackWrapper.accept(() -> action.onWqueueCreated(body.created, toNext));
                    break;
                }
                case "wqueue-updated": {
                    WqueueUpdated body = mapper.readValue(log.body, WqueueUpdated.class);
                    callbackWrapper.accept(() -> action.onWqueueUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "wqueue-deleted": {
                    WqueueDeleted body = mapper.readValue(log.body, WqueueDeleted.class);
                    callbackWrapper.accept(() -> action.onWqueueDeleted(body.deleted, toNext));
                    break;
                }
                case "hoken-updated": {
                    VisitUpdated body = mapper.readValue(log.body, VisitUpdated.class);
                    callbackWrapper.accept(() -> action.onHokenUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "pharma-queue-created": {
                    PharmaQueueCreated body = mapper.readValue(log.body, PharmaQueueCreated.class);
                    callbackWrapper.accept(() -> action.onPharmaQueueCreated(body.created, toNext));
                    break;
                }
                case "pharma-queue-updated": {
                    PharmaQueueUpdated body = mapper.readValue(log.body, PharmaQueueUpdated.class);
                    callbackWrapper.accept(() -> action.onPharmaQueueUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "pharma-queue-deleted": {
                    PharmaQueueDeleted body = mapper.readValue(log.body, PharmaQueueDeleted.class);
                    callbackWrapper.accept(() -> action.onPharmaQueueDeleted(body.deleted, toNext));
                    break;
                }
                case "patient-created": {
                    PatientCreated body = mapper.readValue(log.body, PatientCreated.class);
                    callbackWrapper.accept(() -> action.onPatientCreated(body.created, toNext));
                    break;
                }
                case "patient-updated": {
                    PatientUpdated body = mapper.readValue(log.body, PatientUpdated.class);
                    callbackWrapper.accept(() -> action.onPatientUpdated(body.prev, body.updated, toNext));
                    break;
                }
                case "patient-deleted": {
                    PatientDeleted body = mapper.readValue(log.body, PatientDeleted.class);
                    callbackWrapper.accept(() -> action.onPatientDeleted(body.deleted, toNext));
                    break;
                }
                default: {
                    System.err.println("Unknown kind: " + log.kind);
                    toNext.run();
                }
            }
        } catch (Exception ex) {
            logger.error("Failed to dispatch practice log.", ex);
            throw new RuntimeException(ex);
        }

    }

}

