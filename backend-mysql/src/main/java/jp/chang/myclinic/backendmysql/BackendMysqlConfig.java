package jp.chang.myclinic.backendmysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"jp.chang.myclinic.backendmysql.entity.core", "jp.chang.myclinic.backendmysql.entity.intraclinic"})
@ComponentScan({"jp.chang.myclinic.backendmysql", "jp.chang.myclinic.backendmysql.persistence",
        "jp.chang.myclinic.backendmysql.entity.core", "jp.chang.myclinic.backendmysql.entity.intraclinic"})
@EnableJpaRepositories({"jp.chang.myclinic.backendmysql.entity.core", "jp.chang.myclinic.backendmysql.entity.intraclinic"})
public class BackendMysqlConfig {

}
