package jp.chang.myclinic.apitool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class MysqlConnectionProvider implements Supplier<Connection> {


    @Override
    public Connection get() {
        try {
            String url = "jdbc:mysql://localhost:3306/myclinic?zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&useUnicode=true&characterEncoding=utf8&useSSL=false&useSSL=false&serverTimezone=JST";
            return DriverManager.getConnection(url,
                    System.getenv("MYCLINIC_POSTGRES_USER"), System.getenv("MYCLINIC_POSTGRES_PASS"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
