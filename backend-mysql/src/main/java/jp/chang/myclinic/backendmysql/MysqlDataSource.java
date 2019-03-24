package jp.chang.myclinic.backendmysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDataSource {

    public static class MysqlDataSourceConfig {
        private String host = System.getenv("MYCLINCI_DB_HOST");
        private int port = 3306;
        private String database = "myclinic";
        private boolean useSSL = false;
        private String user = System.getenv("MYCLINIC_DB_USER");
        private String password = System.getenv("MYCLINIC_DB_PASS");

        public MysqlDataSourceConfig host(String host){
            this.host = host;
            return this;
        }

        public MysqlDataSourceConfig port(int port){
            this.port = port;
            return this;
        }

        public MysqlDataSourceConfig database(String database){
            this.database = database;
            return this;
        }

        public MysqlDataSourceConfig user(String user){
            this.user = user;
            return this;
        }

        public MysqlDataSourceConfig password(String password){
            this.password = password;
            return this;
        }
    }

    private MysqlDataSource() { }

    public static DataSource create(){
        return create(new MysqlDataSourceConfig());
    }

    public static DataSource create(MysqlDataSourceConfig mysqlConfig){
        String url = String.format("jdbc:mysql://%s:%d/%s?zeroDateTimeBehavior=convertToNull" +
                        "&noDatetimeStringSync=true&useUnicode=true&characterEncoding=utf8" +
                        "&useSSL=%s&serverTimezone=JST",
                mysqlConfig.host, mysqlConfig.port, mysqlConfig.database, mysqlConfig.useSSL ? "true" : "false");
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(mysqlConfig.user);
        config.setPassword(mysqlConfig.password);
        HikariDataSource ds = new HikariDataSource(config);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("closing data souce");
            ds.close();
        }));
        return ds;
    }

}
