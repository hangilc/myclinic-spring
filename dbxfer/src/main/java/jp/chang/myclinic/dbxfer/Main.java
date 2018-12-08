package jp.chang.myclinic.dbxfer;

import jp.chang.myclinic.dbxfer.db.TableXferer;

import java.sql.Connection;

public class Main {

    public static void main( String[] args )
    {
    }

    private Connection srcConn;
    private Connection dstConn;
    private MySqlDb dstDb = new MySqlDb();
    private PostgreSqlDb srcDb = new PostgreSqlDb();
    private Converter conv = new Converter();

    void xferIyakuhinMaster(){
        TableXferer xferer = new TableXferer(srcConn, srcDb.iyakuhinMaster, dstConn, dstDb.iyakuhinMaster);
        xferer.addColumn(srcDb.iyakuhinMaster.iyakuhincode, dstDb.iyakuhinMaster.iyakuhincode);
        xferer.addColumn(srcDb.iyakuhinMaster.name, dstDb.iyakuhinMaster.name);
        xferer.addColumn(srcDb.iyakuhinMaster.yomi, dstDb.iyakuhinMaster.yomi);
        xferer.addColumn(srcDb.iyakuhinMaster.unit, dstDb.iyakuhinMaster.unit);
        xferer.addColumn(srcDb.iyakuhinMaster.yakka, dstDb.iyakuhinMaster.yakka, conv::stringToBigDecimal);
        xferer.addColumn(srcDb.iyakuhinMaster.madoku, dstDb.iyakuhinMaster.madoku);
        xferer.addColumn(srcDb.iyakuhinMaster.kouhatsu, dstDb.iyakuhinMaster.kouhatsu);
        xferer.addColumn(srcDb.iyakuhinMaster.zaikei, dstDb.iyakuhinMaster.zaikei);
        xferer.addColumn(srcDb.iyakuhinMaster.validFrom, dstDb.iyakuhinMaster.validFrom, conv::stringToLocalDate);
        xferer.addColumn(srcDb.iyakuhinMaster.validUpto, dstDb.iyakuhinMaster.validUpto, conv::oldSqldateToLocalDate);
        xferer.xfer();
    }

}

