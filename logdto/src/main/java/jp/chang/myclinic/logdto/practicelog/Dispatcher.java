package jp.chang.myclinic.logdto.practicelog;


import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.ChargeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Dispatcher {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private static ObjectMapper mapper = new ObjectMapper();

    public void dispatch(List<PracticeLog> practiceLogList){
        try {
            for(PracticeLog practiceLog: practiceLogList) {
                switch (practiceLog.kind) {
                    case "charge-created": {
                        ChargeCreated body = mapper.readValue(practiceLog.body, ChargeCreated.class);
                        onChargeCreated(body.created);
                        break;
                    }
                    case "charge-updated": {
                        ChargeUpdated body = mapper.readValue(practiceLog.body, ChargeUpdated.class);
                        onChargeUpdated(body.prev, body.updated);
                        break;
                    }
                    case "charge-deleted": {
                        ChargeDeleted body = mapper.readValue(practiceLog.body, ChargeDeleted.class);
                        onChargeDeleted(body.deleted);
                        break;
                    }
                    default:
                        logger.error("Unknown practice log: " + practiceLog.kind);
                }
            }
        } catch (Exception e) {
            logger.error("Failed to parse practice log.", e);
            throw new RuntimeException("Error in parseing practice log");
        }
    }

    public void onChargeCreated(ChargeDTO created){}
    public void onChargeUpdated(ChargeDTO prev, ChargeDTO updated){}
    public void onChargeDeleted(ChargeDTO deleted){}
}
