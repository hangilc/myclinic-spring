package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.ConverterMap;
import jp.chang.myclinic.dbxfer.db.TableXferer;
import jp.chang.myclinic.dbxfer.mysql.ConverterMapMySql;

import java.sql.Connection;

public class Main {

    public static void main( String[] args )
    {
        new Main().xferAll();
    }

    private Connection srcConn;
    private Connection dstConn;
    private MySqlDb dstDb = new MySqlDb();
    private PostgreSqlDb srcDb = new PostgreSqlDb();
    private ConverterMap convMap = new ConverterMapMySql();
    private TableXferer tableXferer;

    Main(){

    }

    void xferAll(){
        tableXferer.xfer(srcDb.iyakuhinMaster, dstDb.iyakuhinMaster);
    }

}

