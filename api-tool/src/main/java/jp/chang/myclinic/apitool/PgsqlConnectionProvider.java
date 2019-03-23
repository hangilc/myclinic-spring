package jp.chang.myclinic.apitool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Supplier;

public class PgsqlConnectionProvider implements Supplier<Connection> {

    @Override
    public Connection get(){
        try {
            return DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                    System.getenv("MYCLINIC_POSTGRES_USER"), System.getenv("MYCLINIC_POSTGRES_PASS"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
