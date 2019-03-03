package jp.chang.myclinic.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.logdto.hotline.HotlineBeep;
import jp.chang.myclinic.logdto.hotline.HotlineCreated;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import jp.chang.myclinic.logdto.HotlineLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
class HotlineLogPublisher implements HotlineLogger.HotlineLogPublisher, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(HotlineLogPublisher.class);
    private static ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private DbGatewayInterface dbGateway;
    @Autowired
    @Qualifier("hotline-logger")
    private PublishingWebSocketHandler hotlineLogHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        sendLastLog();
    }

    @Override
    public void publishCreated(HotlineDTO hotlineDTO) {
        publish("hotline-created", new HotlineCreated(hotlineDTO));
    }

    @Override
    public void publishBeep(String receiver) {
        publish("hotline-beep", new HotlineBeep(receiver));
    }

    private void publish(String kind, Object value){
        try {
            HotlineLogDTO log = new HotlineLogDTO();
            log.kind = kind;
            log.body = mapper.writeValueAsString(value);
            hotlineLogHandler.publish(mapper.writeValueAsString(log));
        } catch(Exception ex){
            logger.error("Failed to send message.", ex);
        }
    }

    private void sendLastLog() {
        dbGateway.getTodaysLastHotline().ifPresent(dto -> {
            logger.info("last hotline log: {}", dto);
            publishCreated(dto);
        });
    }


}
