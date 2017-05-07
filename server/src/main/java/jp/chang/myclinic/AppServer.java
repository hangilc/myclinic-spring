package jp.chang.myclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import jp.chang.myclinic.rcpt.HoukatsuKensa;

@SpringBootApplication
public class AppServer {
    public static void main( String[] args )
    {
        SpringApplication.run(AppServer.class, args);
    }

    @Bean
    public HoukatsuKensa makeHoukatsuKensa(){
    	return HoukatsuKensa.load();
    }

}
