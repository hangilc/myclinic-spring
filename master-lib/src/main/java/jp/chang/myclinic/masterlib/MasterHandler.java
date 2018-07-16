package jp.chang.myclinic.masterlib;

import java.sql.SQLException;
import java.sql.Statement;

class MasterHandler {

    private Statement stmt;

    MasterHandler(Statement stmt) {
        this.stmt = stmt;
    }

    boolean enterShinryouMaster(ShinryouMasterCSV shinryouCSV, String validFrom) throws SQLException {
        int kubun = shinryouCSV.kubun;
        if (kubun == 0 || kubun == 3 || kubun == 5) {
            String sql = shinryouSql(shinryouCSV, validFrom);
            stmt.executeUpdate(sql);
            return true;
        } else {
            return false;
        }

    }

    boolean enterIyakuhinMaster(IyakuhinMasterCSV iyakuhinCSV, String validFrom) throws SQLException {
        int kubun = iyakuhinCSV.kubun;
        if (kubun == 0 || kubun == 3 || kubun == 5) {
            String sql = iyakuhinSql(iyakuhinCSV, validFrom);
            stmt.executeUpdate(sql);
            return true;
        } else {
            return false;
        }

    }

    private static String shinryouTemplate;

    static {
        shinryouTemplate = String.join(" ",
                "insert into shinryoukoui_master_arch set",
                "shinryoucode=%s,",
                "name='%s',",
                "tensuu='%s',",
                "tensuu_shikibetsu='%s',",
                "shuukeisaki='%s',",
                "houkatsukensa='%s',",
                "oushinkubun='%s',",
                "kensagroup='%s',",
                "valid_from='%s',",
                "valid_upto='%s';"
        );
    }

    private static String shinryouSql(ShinryouMasterCSV master, String validFrom) {
        return String.format(shinryouTemplate,
                master.shinryoucode,
                master.name,
                master.tensuu,
                master.tensuuShikibetsu,
                master.shuukeisaki,
                master.houkatsukensa,
                master.oushinKubun,
                master.kensaGroup,
                validFrom,
                "0000-00-00"
        );
    }

    private static String iyakuhinTemplate;

    static {
        StringBuilder builder = new StringBuilder();
        builder.append("insert into iyakuhin_master_arch set ");
        builder.append("iyakuhincode=%d,");
        builder.append("yakkacode='%s',");
        builder.append("name='%s',");
        builder.append("yomi='%s',");
        builder.append("unit='%s',");
        builder.append("yakka='%s',");
        builder.append("madoku='%s',");
        builder.append("kouhatsu='%s',");
        builder.append("zaikei='%s',");
        builder.append("valid_from='%s',");
        builder.append("valid_upto='%s';");
        iyakuhinTemplate = builder.toString();
    }

    private static String iyakuhinSql(IyakuhinMasterCSV master, String validFrom) {
        return String.format(iyakuhinTemplate,
                master.iyakuhincode,
                master.yakkacode,
                master.name,
                master.yomi,
                master.unit,
                master.yakka,
                master.madoku,
                master.kouhatsu,
                master.zaikei,
                validFrom,
                "0000-00-00");
    }

}
