package jp.chang.myclinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppServer {
    public static void main( String[] args )
    {
    	jp.chang.myclinic.rcpt.HoukatsuKensa.read();
        //SpringApplication.run(AppServer.class, args);
    }
}
