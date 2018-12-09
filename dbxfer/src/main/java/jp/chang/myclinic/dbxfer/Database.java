package jp.chang.myclinic.dbxfer;

import java.sql.Connection;

class Database {

    private Connection conn;
    private MyclinicDb db;

    Database(Connection conn, MyclinicDb db) {
        this.conn = conn;
        this.db = db;
    }

    public Connection getConn() {
        return conn;
    }

    public MyclinicDb getDb() {
        return db;
    }
}
