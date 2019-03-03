package jp.chang.myclinic.logdto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.logdto.hotline.HotlineBeep;
import jp.chang.myclinic.logdto.hotline.HotlineCreated;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HotlineLogger {

    public interface HotlineLogPublisher {
        void publishCreated(HotlineDTO hotlineDTO);
        void publishBeep(String receiver);
    }

    private static Logger logger = LoggerFactory.getLogger(HotlineLogger.class);
    private HotlineLogPublisher publisher =new HotlineLogPublisher() {
        @Override
        public void publishCreated(HotlineDTO hotlineDTO) {
            // nop
        }

        @Override
        public void publishBeep(String receiver) {
            // nop
        }
    };
    private static ObjectMapper mapper = new ObjectMapper();

    public HotlineLogger() {

    }

    public HotlineLogger(HotlineLogPublisher publisher){
        this.publisher = publisher;
    }

    public void logHotlineCreated(HotlineDTO hotline){
        //logValue("hotline-created", new HotlineCreated(hotline));
        publisher.publishCreated(hotline);
    }

    public void logBeep(String receiver){
        //logValue("hotline-beep", new HotlineBeep(receiver));
        publisher.publishBeep(receiver);
    }

}
