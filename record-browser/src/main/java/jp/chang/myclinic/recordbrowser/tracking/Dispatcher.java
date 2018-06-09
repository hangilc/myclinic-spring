package jp.chang.myclinic.recordbrowser.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLog;
import jp.chang.myclinic.logdto.practicelog.TextCreated;
import jp.chang.myclinic.logdto.practicelog.WqueueUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Dispatcher {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();

    Dispatcher() {

    }

    public void dispatch(List<PracticeLog> logs, DispatchAction action, Runnable cb) {
        iter(0, logs, action, cb);
    }

    private void iter(int i, List<PracticeLog> logs, DispatchAction action, Runnable cb) {
        if (i >= logs.size()) {
            cb.run();
        } else {
            PracticeLog log = logs.get(i);
            Runnable toNext = () -> iter(i+1, logs, action, cb);
            try {
                switch (log.kind) {
                    case "text-created": {
                        TextCreated body = mapper.readValue(log.body, TextCreated.class);
                        action.onTextCreated(body.text, toNext);
                        break;
                    }
                    case "wqueue-updated": {
                        WqueueUpdated body = mapper.readValue(log.body, WqueueUpdated.class);
                        action.onWqueueUpdated(body.prev, body.updated, toNext);
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

