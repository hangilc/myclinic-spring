package jp.chang.myclinic.logdto.practicelog;


import jp.chang.myclinic.dto.ChargeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dispatcher {

    private static Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public void dispatch(PracticeLog practiceLog){
        switch(practiceLog.kind){
            case "change-created": {
                break;
            }
            default: logger.error("Unknown practice log: " + practiceLog.kind);
        }
    }

    public void onChargeCreated(ChargeDTO created){}


}
