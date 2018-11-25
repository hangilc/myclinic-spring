package jp.chang.myclinic.serverpostgresql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main( String[] args )
    {
        boolean isTest = false;
        for(String arg: args){
            if( "--test".equals(arg) ){
                isTest = true;
            }
        }
        SpringApplication application = new SpringApplication(AppConfig.class);
        if( isTest ){
            application.setAdditionalProfiles("postgresqltest");
        } else {
            application.setAdditionalProfiles("postgresql");
        }
        application.run(args);
    }


}

