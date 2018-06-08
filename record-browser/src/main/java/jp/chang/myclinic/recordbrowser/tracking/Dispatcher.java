package jp.chang.myclinic.recordbrowser.tracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.logdto.practicelog.PracticeLog;
import jp.chang.myclinic.logdto.practicelog.WqueueUpdated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Dispatcher {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ObjectMapper mapper = new ObjectMapper();

    Dispatcher() {

    }

    public void dispatch(List<PracticeLog>logs){
        try {
            for(PracticeLog log: logs){
                switch(log.kind){
                    case "wqueue-updated": {
                        WqueueUpdated body = mapper.readValue(log.body, WqueueUpdated.class);
                        onWqueueUpdated(body.prev, body.updated);
                        break;
                    }
                    default: {
                        System.err.println("Unknown kind: " + log.kind);
                    }
                }
            }
        } catch(Exception ex){
            logger.error("Failed to dispatch practice log.", ex);
            throw new RuntimeException(ex);
        }
    }

    protected void onWqueueUpdated(WqueueDTO prev, WqueueDTO updated){
        
    }

}
