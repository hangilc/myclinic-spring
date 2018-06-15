package jp.chang.myclinic.recordbrowser.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import jp.chang.myclinic.logdto.practicelog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class Dispatcher implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();
    private Semaphore taskPermit = new Semaphore(1);
    private BlockingQueue<PracticeLogDTO> pendings = new LinkedBlockingQueue<>();
    private DispatchAction action;
    private Runnable toNext;

    public Dispatcher(DispatchAction action) {
        this.action = action;
        toNext = () -> taskPermit.release();
    }

    public void add(PracticeLogDTO plog){
        try {
            pendings.put(plog);
        } catch (InterruptedException e) {
            logger.error("Failed to add practice log.", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(){
        //noinspection InfiniteLoopStatement
        while(true){
            try {
                PracticeLogDTO plog = pendings.take();
                taskPermit.acquire();
                Platform.runLater(() -> dispatchOne(plog));
            } catch (InterruptedException e) {
                logger.info("Dispatcher interrupted.", e);
            }
        }
    }

    private void dispatchOne(PracticeLogDTO log){
        try {
            switch (log.kind) {
                case "visit-created": {
                    VisitCreated body = mapper.readValue(log.body, VisitCreated.class);
                    action.onVisitCreated(body.created, toNext);
                    break;
                }
                case "text-created": {
                    TextCreated body = mapper.readValue(log.body, TextCreated.class);
                    action.onTextCreated(body.text, toNext);
                    break;
                }
                case "text-updated": {
                    TextUpdated body = mapper.readValue(log.body, TextUpdated.class);
                    action.onTextUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "text-deleted": {
                    TextDeleted body = mapper.readValue(log.body, TextDeleted.class);
                    action.onTextDeleted(body.deleted, toNext);
                    break;
                }
                case "drug-created": {
                    DrugCreated body = mapper.readValue(log.body, DrugCreated.class);
                    action.onDrugCreated(body.created, toNext);
                    break;
                }
                case "drug-updated": {
                    DrugUpdated body = mapper.readValue(log.body, DrugUpdated.class);
                    action.onDrugUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "drug-deleted": {
                    DrugDeleted body = mapper.readValue(log.body, DrugDeleted.class);
                    action.onDrugDeleted(body.deleted, toNext);
                    break;
                }
                case "shinryou-created": {
                    ShinryouCreated body = mapper.readValue(log.body, ShinryouCreated.class);
                    action.onShinryouCreated(body.created, toNext);
                    break;
                }
                case "shinryou-deleted": {
                    ShinryouDeleted body = mapper.readValue(log.body, ShinryouDeleted.class);
                    action.onShinryouDeleted(body.deleted, toNext);
                    break;
                }
                case "conduct-created": {
                    ConductCreated body = mapper.readValue(log.body, ConductCreated.class);
                    action.onConductCreated(body.created, toNext);
                    break;
                }
                case "conduct-updated": {
                    ConductUpdated body = mapper.readValue(log.body, ConductUpdated.class);
                    action.onConductUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "gazou-label-created": {
                    GazouLabelCreated body = mapper.readValue(log.body, GazouLabelCreated.class);
                    action.onGazouLabelCreated(body.created, toNext);
                    break;
                }
                case "gazou-label-updated": {
                    GazouLabelUpdated body = mapper.readValue(log.body, GazouLabelUpdated.class);
                    action.onGazouLabelUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "conduct-shinryou-created": {
                    ConductShinryouCreated body = mapper.readValue(log.body, ConductShinryouCreated.class);
                    action.onConductShinryouCreated(body.created, toNext);
                    break;
                }
                case "conduct-shinryou-deleted": {
                    ConductShinryouDeleted body = mapper.readValue(log.body, ConductShinryouDeleted.class);
                    action.onConductShinryouDeleted(body.deleted, toNext);
                    break;
                }
                case "conduct-drug-created": {
                    ConductDrugCreated body = mapper.readValue(log.body, ConductDrugCreated.class);
                    action.onConductDrugCreated(body.created, toNext);
                    break;
                }
                case "conduct-drug-deleted": {
                    ConductDrugDeleted body = mapper.readValue(log.body, ConductDrugDeleted.class);
                    action.onConductDrugDeleted(body.deleted, toNext);
                    break;
                }
                case "conduct-kizai-created": {
                    ConductKizaiCreated body = mapper.readValue(log.body, ConductKizaiCreated.class);
                    action.onConductKizaiCreated(body.created, toNext);
                    break;
                }
                case "conduct-kizai-deleted": {
                    ConductKizaiDeleted body = mapper.readValue(log.body, ConductKizaiDeleted.class);
                    action.onConductKizaiDeleted(body.deleted, toNext);
                    break;
                }
                case "charge-created": {
                    ChargeCreated body = mapper.readValue(log.body, ChargeCreated.class);
                    action.onChargeCreated(body.created, toNext);
                    break;
                }
                case "charge-updated": {
                    ChargeUpdated body = mapper.readValue(log.body, ChargeUpdated.class);
                    action.onChargeUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "payment-created": {
                    PaymentCreated body = mapper.readValue(log.body, PaymentCreated.class);
                    action.onPaymentCreated(body.created, toNext);
                    break;
                }
                case "wqueue-updated": {
                    WqueueUpdated body = mapper.readValue(log.body, WqueueUpdated.class);
                    action.onWqueueUpdated(body.prev, body.updated, toNext);
                    break;
                }
                case "hoken-updated": {
                    VisitUpdated body = mapper.readValue(log.body, VisitUpdated.class);
                    action.onHokenUpdated(body.prev, body.updated, toNext);
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

