package jp.chang.myclinic.serverpostgresql;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableJpaRepositories(basePackages = {"jp.chang.myclinic.serverpostgresql.db.myclinic"})
@ComponentScan(basePackages="jp.chang.myclinic.serverpostgresql.db")
class PersistenceConfig {

    //private static Logger logger = LoggerFactory.getLogger(PersistenceConfig.class);

    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("jp.chang.myclinic.serverpostgresql.db.myclinic");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    //@Primary
    public DataSource dataSource(){
        return DataSourceBuilder.create()
                .url("jdbc:postgresql://localhost:5432/myclinic")
                .username(System.getenv("MYCLINIC_DB_USER"))
                .password(System.getenv("MYCLINIC_DB_PASS"))
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(
            SessionFactory sessionFactory) {

        HibernateTransactionManager txManager
                = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    private Properties hibernateProperties(){
        return new Properties(){
            {
                setProperty("jdbc.lob.non_contextual_creation", "true");
                setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            }
        };
    }

}
