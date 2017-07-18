package jp.chang.myclinic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({ "classpath:persistence-myclinic-db.properties"})
@EnableJpaRepositories(
        basePackages = "jp.chang.myclinic.db.myclinic",
        entityManagerFactoryRef = "myclinicEntityManager",
        transactionManagerRef = "myclinicTransactionManager"
)
public class MyclinicConfig {

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean myclinicEntityManager(){
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(myclinicDataSource());
        em.setPackagesToScan("jp.chang.myclinic.db.myclinic");
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean
    public DataSource myclinicDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
    }
}
