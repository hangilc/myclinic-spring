package jp.chang.myclinic.serverpostgresql;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.serverpostgresql.rcpt.HoukatsuKensa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Configuration
@ComponentScan
class AppConfig {

    private static Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public MasterMap getMasterMap(@Value("${myclinic.master-map-file}") String masterMapLocation,
                                  @Value("${myclinic.name-map-file}") String nameMapLocation) throws IOException {
        return MasterMap.loadMap(nameMapLocation, masterMapLocation);
    }

    @Bean
    public HoukatsuKensa makeHoukatsuKensa() throws IOException {
        return HoukatsuKensa.load();
    }

    @Bean(name="practice-logger")
    public PublishingWebSocketHandler getPracticeLogHandler(){
        return new PublishingWebSocketHandler();
    }

    @Bean(name="hotline-logger")
    public PublishingWebSocketHandler getHotlineLogHandler(){
        return new PublishingWebSocketHandler();
    }

    @Bean
    public ReferList getReferList(){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            List<ReferList.ReferItem> items = mapper.readValue(new File("./config/refer-list.yml"),
                    new TypeReference<List<ReferList.ReferItem>>(){});
            ReferList referList = new ReferList();
            referList.setList(items);
            return referList;
        } catch(Exception ex){
            logger.error("Failed to load refer list.", ex);
            return null;
        }
    }


}
