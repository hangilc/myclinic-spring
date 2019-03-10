package jp.chang.myclinic.backendpgsql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.function.Consumer;
import java.util.function.Function;

public class PersistenceBase {

    private ThreadLocal<Connection> local;

    public void setConnection(Connection conn){
        local.set(conn);
    }

    public <T> T with(Function<Connection, T> f){
        Connection conn = local.get();
        if( conn == null ){
            return DB.get(f::apply);
        } else {
            return f.apply(conn);
        }
    }

    public void execWith(Consumer<Connection> p){
        Connection conn = local.get();
        if( conn == null ){
            DB.get(connection -> { p.accept(connection); return null; });
        } else {
            p.accept(conn);
        }
    }

}
