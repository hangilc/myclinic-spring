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

//        Properties properties = new Properties();
//        properties.put("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation", true);
//        properties.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
//        properties.put("spring.jpa.properties.hibernate.show_sql", true);
//        properties.put("spring.jpa.properties.hibernate.format_sql", true);
//        application.setDefaultProperties(properties);

        application.run(args);
    }

//    @Bean
//    @Primary
//    public DataSource getDataSource(){
//        return DataSourceBuilder.create()
//                .url("jdbc:postgresql://localhost:5432/myclinic")
//                .username(System.getenv("MYCLINIC_DB_USER"))
//                .password(System.getenv("MYCLINIC_DB_PASS"))
//                .driverClassName("org.postgresql.Driver")
//                .build();
//    }

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

