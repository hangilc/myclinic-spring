package jp.chang.myclinic.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dto.HotlineDTO;
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
    public void afterPropertiesSet() throws Exception {
        sendLastLog();
    }

    private void sendLastLog() throws Exception {
        HotlineDTO dto = dbGateway.getLastHotline();
        logger.info("last hotline log: {}", dto);
        if( dto != null ) {
            hotlineLogHandler.publish(mapper.writeValueAsString(dto));
        }
    }

    public void logHotline(HotlineDTO hotline){
        try {
            hotlineLogHandler.publish(mapper.writeValueAsString(hotline));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
