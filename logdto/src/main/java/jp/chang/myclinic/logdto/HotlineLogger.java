package jp.chang.myclinic.logdto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.logdto.hotline.HotlineBeep;
import jp.chang.myclinic.logdto.hotline.HotlineCreated;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotlineLogger {

    public interface HotlineLogPublisher {
        void publish(String message);
    }

    private HotlineLogPublisher publisher = message -> {};
    private static ObjectMapper mapper = new ObjectMapper();

    public HotlineLogger() {

    }

    public HotlineLogger(HotlineLogPublisher publisher){
        this.publisher = publisher;
    }

    public void setHotlineLogPublisher(HotlineLogPublisher publisher){
        this.publisher = publisher;
    }

    private void logValue(String kind, Object value){
        HotlineLogDTO log = new HotlineLogDTO();
        log.kind = kind;
        try {
            log.body = mapper.writeValueAsString(value);
            publisher.publish(mapper.writeValueAsString(log));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void logHotlineCreated(HotlineDTO hotline){
        logValue("hotline-created", new HotlineCreated(hotline));
    }

    public void logBeep(String receiver){
        logValue("hotline-beep", new HotlineBeep(receiver));
    }

}
