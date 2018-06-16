package jp.chang.myclinic.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.drawer.JacksonOpSerializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.mastermap.MasterMap;
import jp.chang.myclinic.server.rcpt.HoukatsuKensa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class AppServer implements CommandLineRunner{

    private Logger logger = LoggerFactory.getLogger(AppServer.class);

    @Autowired
    private DataSource dataSource;

    public static void main( String[] args )
    {
        SpringApplication.run(AppServer.class, args);
    }

    @Bean
    public HoukatsuKensa makeHoukatsuKensa() throws IOException {
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
        return MasterMap.loadMap(nameMapLocation, masterMapLocation);
    }

    @Bean
    public ObjectMapper getObjectMapper(){
        return new ObjectMapper();
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

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("DataSource: " + dataSource);
    }
}
