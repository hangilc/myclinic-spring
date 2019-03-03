package jp.chang.myclinic.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.logdto.HotlineLogger;
import jp.chang.myclinic.logdto.hotline.HotlineLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
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
    public void publish(String message) {
        try {
            hotlineLogHandler.publish(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendLastLog() {
        dbGateway.getTodaysLastHotline().ifPresent(dto -> {
            logger.info("last hotline log: {}", dto);
            HotlineLogDTO log = new HotlineLogDTO();
            log.kind = "hotline-created";
            try {
                log.body = mapper.writeValueAsString(dto);
                publish(mapper.writeValueAsString(log));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }


}
