package jp.chang.myclinic.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.logdto.hotline.HotlineBeep;
import jp.chang.myclinic.logdto.hotline.HotlineCreated;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
import jp.chang.myclinic.server.db.myclinic.DbGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class HotlineLogger implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(HotlineLogger.class);

    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private DbGateway dbGateway;
    @Autowired
    @Qualifier("hotline-logger")
    private PublishingWebSocketHandler hotlineLogHandler;

    @Override
    public void afterPropertiesSet() {
        sendLastLog();
    }

    private void sendLastLog() {
        dbGateway.getTodaysLastHotline().ifPresent(dto -> {
            logger.info("last hotline log: {}", dto);
            logHotlineCreated(dto);

        });
    }

    private void logValue(String kind, Object value){
        try {
            HotlineLogDTO log = new HotlineLogDTO();
            log.kind = kind;
            log.body = mapper.writeValueAsString(value);
            hotlineLogHandler.publish(mapper.writeValueAsString(log));
        } catch(Exception ex){
            logger.error("Failed to send message.", ex);
        }
    }

    public void logHotlineCreated(HotlineDTO hotline){
        logValue("hotline-created", new HotlineCreated(hotline));
    }

    public void logBeep(String receiver){
        logValue("hotline-beep", new HotlineBeep(receiver));
    }

}
