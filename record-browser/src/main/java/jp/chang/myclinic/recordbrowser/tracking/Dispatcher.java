package jp.chang.myclinic.recordbrowser.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.logdto.practicelog.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Dispatcher {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();

    Dispatcher() {

    }

    public void dispatch(List<PracticeLogDTO> logs, DispatchAction action, Runnable cb) {
        iter(0, logs, action, cb);
    }

    private void iter(int i, List<PracticeLogDTO> logs, DispatchAction action, Runnable cb) {
        if (i >= logs.size()) {
            cb.run();
        } else {
            PracticeLogDTO log = logs.get(i);
            Runnable toNext = () -> iter(i + 1, logs, action, cb);
            try {
                switch (log.kind) {
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
                    case "shinryou-created": {
                        ShinryouCreated body = mapper.readValue(log.body, ShinryouCreated.class);
                        action.onShinryouCreated(body.created, toNext);
                        break;
                    }
                    case "conduct-created": {
                        ConductCreated body = mapper.readValue(log.body, ConductCreated.class);
                        action.onConductCreated(body.created, toNext);
                        break;
                    }
                    case "gazou-label-created": {
                        GazouLabelCreated body = mapper.readValue(log.body, GazouLabelCreated.class);
                        action.onGazouLabelCreated(body.created, toNext);
                        break;
                    }
                    case "conduct-shinryou-created": {
                        ConductShinryouCreated body = mapper.readValue(log.body, ConductShinryouCreated.class);
                        action.onConductShinryouCreated(body.created, toNext);
                        break;
                    }
                    case "conduct-drug-created": {
                        ConductDrugCreated body = mapper.readValue(log.body, ConductDrugCreated.class);
                        action.onConductDrugCreated(body.created, toNext);
                        break;
                    }
                    case "conduct-kizai-created": {
                        ConductKizaiCreated body = mapper.readValue(log.body, ConductKizaiCreated.class);
                        action.onConductKizaiCreated(body.created, toNext);
                        break;
                    }
                    case "charge-created": {
                        ChargeCreated body = mapper.readValue(log.body, ChargeCreated.class);
                        action.onChargeCreated(body.created, toNext);
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
}

