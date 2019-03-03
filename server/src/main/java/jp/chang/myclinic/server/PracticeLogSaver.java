package jp.chang.myclinic.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.logdto.PracticeLogger;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Objects;

@Component
class PracticeLogSaver implements PracticeLogger.PracticeLogSaver, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(PracticeLogSaver.class);
    private ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private DbGatewayInterface dbGateway;
    @Autowired
    @Qualifier("practice-logger")
    private PublishingWebSocketHandler practiceLogHandler;

    @Override
    public void afterPropertiesSet() throws Exception {
        sendLastLog();
    }

    @Override
    @Transactional
    public void save(PracticeLogDTO dto) {
        dbGateway.enterPracticeLog(dto);
        try {
            practiceLogHandler.publish(mapper.writeValueAsString(dto));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void sendLastLog() throws Exception {
        PracticeLogDTO lastLog = dbGateway.findLastPracticeLog();
        logger.info("last practice log: {}", lastLog);
        if( lastLog == null ){
            return;
        }
        try {
            LocalDate date = LocalDate.parse(lastLog.createdAt.substring(0, 10));
            if( Objects.equals(date, LocalDate.now() ) ){
                logger.info("todays last practice log sent.");
                practiceLogHandler.publish(mapper.writeValueAsString(lastLog));
            }
        } catch(Exception ex){
            logger.error("Failed to get last log", ex);
        }
    }

}
