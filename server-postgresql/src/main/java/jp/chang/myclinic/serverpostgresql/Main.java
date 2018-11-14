package jp.chang.myclinic.serverpostgresql;

import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.serverpostgresql.rcpt.HoukatsuKensa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootApplication
public class Main {

    public static void main( String[] args )
    {
        SpringApplication application = new SpringApplication(Main.class);
        application.setAdditionalProfiles("postgresql");
        application.run(args);
    }

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


}

