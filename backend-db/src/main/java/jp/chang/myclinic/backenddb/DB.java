package jp.chang.myclinic.backenddb;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Supplier;

public interface DB {

    Supplier<Connection> getConnectionProvider();

    interface Proc<T> {
        T call() throws SQLException;
    }

    <T> T query(Proc<T> proc);

    <T> T tx(Proc<T> proc);

}
