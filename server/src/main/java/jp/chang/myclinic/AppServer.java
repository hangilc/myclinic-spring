package jp.chang.myclinic;

import jp.chang.myclinic.drawer.JacksonOpSerializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.rcpt.HoukatsuKensa;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

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

    @Bean
    public MasterMap getMasterMap(@Value("${myclinic.master-map-file}") String masterMapLocation,
                                  @Value("${myclinic.name-map-file}") String nameMapLocation) throws IOException {
        MasterMap masterMap = new MasterMap();
        {
            Stream<String> lines = Files.lines(Paths.get(masterMapLocation));
            masterMap.loadCodeMap(lines);
        }
        {
            Stream<String> lines = Files.lines(Paths.get(nameMapLocation));
            masterMap.loadNameMap(lines);
        }
        return masterMap;
    }

}
