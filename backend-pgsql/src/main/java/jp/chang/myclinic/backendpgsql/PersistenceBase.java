package jp.chang.myclinic.backendpgsql;

import java.sql.Connection;

public class PersistenceBase {

    private final Connection conn;

    protected PersistenceBase(Connection conn){
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }

}
