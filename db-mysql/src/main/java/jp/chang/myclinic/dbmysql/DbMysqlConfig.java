package jp.chang.myclinic.dbmysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"jp.chang.myclinic.dbmysql.core", "jp.chang.myclinic.dbmysql.intraclinic"})
@ComponentScan({"jp.chang.myclinic.dbmysql",
        "jp.chang.myclinic.dbmysql.core", "jp.chang.myclinic.dbmysql.intraclinic"})
@EnableJpaRepositories("jp.chang.myclinic.dbmysql")
public class DbMysqlConfig {


}
