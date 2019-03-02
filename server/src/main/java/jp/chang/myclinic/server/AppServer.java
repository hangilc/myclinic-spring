package jp.chang.myclinic.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dbmysql.DbGateway;
import jp.chang.myclinic.dbmysql.DbMysqlConfig;
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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication()
@Import(DbMysqlConfig.class)
public class AppServer implements CommandLineRunner{

    private Logger logger = LoggerFactory.getLogger(AppServer.class);

    @Autowired
    private DataSource dataSource;
    @Autowired
    private jp.chang.myclinic.dbmysql.DbGateway dbGatewayMySql;

    public static void main( String[] args )
    {
        SpringApplication.run(AppServer.class, args);
    }

    @Bean
    public DbGatewayInterface getDbGateway(){
        return dbGatewayMySql;
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

    @Bean(name="practice-logger")
    public PublishingWebSocketHandler getPracticeLogHandler(){
        return new PublishingWebSocketHandler();
    }

    @Bean(name="hotline-logger")
    public PublishingWebSocketHandler getHotlineLogHandler(){
        return new PublishingWebSocketHandler();
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("DataSource: " + dataSource);
    }
}
