package jp.chang.myclinic.backendpgsql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

class PgsqlDataSource {

    public static DataSource create(){
        HikariConfig config = new HikariConfig();
        String host = System.getenv("MYCLINIC_POSTGRES_HOST");
        String user = System.getenv("MYCLINIC_POSTGRES_USER");
        String pass = System.getenv("MYCLINIC_POSTGRES_PASS");
        config.setJdbcUrl("jdbc:postgresql://" + host + "/myclinic");
        config.setUsername(user);
        config.setPassword(pass);
        HikariDataSource ds = new HikariDataSource(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("closing data souce");
            ds.close();
        }));
        return ds;
    }

}
