package jp.chang.myclinic;

import jp.chang.myclinic.drawer.JacksonOpSerializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.rcpt.HoukatsuKensa;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer addDrawerOpDeserializer(){
        return builder -> {
            builder.serializerByType(Op.class, new JacksonOpSerializer());
        };
    }

}
