package jp.chang.myclinic.postgresqldev;

import java.math.BigDecimal;
import java.sql.*;

public class Main {

    private Connection mysqlConn;
    private Connection psqlConn;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        //main.moveIyakuhinMaster(false);
        //main.moveShinryouMaster(false);
        //main.moveKizaiMaster(true);
    }

    private Main() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost/myclinic?useSSL=false&zeroDateTimeBehavior=convertToNull",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
        this.psqlConn = DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
    }

    private  void moveKizaiMaster(boolean printRow) throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from tokuteikizai_master_arch");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into kizai_master " +
                "(kizaicode, name, yomi, unit, kingaku, valid_from, valid_upto) " +
                "values (?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        while (rset.next()) {
            int kizaicode = rset.getInt("kizaicode");
            String name = rset.getString("name");
            String yomi = rset.getString("yomi");
            String unit = rset.getString("unit");
            String kingaku = rset.getString("kingaku");
            String validFrom = rset.getString("valid_from");
            String validUpto = rset.getString("valid_upto");
            if( printRow ) {
                System.out.printf("%d %s %s %s %s %s %s\n", kizaicode, name, yomi,
                        unit, kingaku, validFrom, validUpto);
            }
            psqlStmt.setInt(1, kizaicode);
            psqlStmt.setString(2, name);
            psqlStmt.setString(3, yomi);
            psqlStmt.setString(4, unit);
            psqlStmt.setBigDecimal(5, new BigDecimal(kingaku));
            psqlStmt.setDate(6, Date.valueOf(validFrom));
            psqlStmt.setDate(7, convertValidUpto(validUpto));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("kizai_master %d\n", n);
            }
        }
        System.out.printf("kizai_master %d\n", n);
        rset.close();
        stmt.close();
        psqlStmt.close();
    }
    private  void moveShinryouMaster(boolean printRow) throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from shinryoukoui_master_arch");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into shinryou_master " +
                "(shinryoucode, name, tensuu, tensuu_shikibetsu, shuukeisaki, houkatsukensa, valid_from, valid_upto) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        while (rset.next()) {
            int shinryoucode = rset.getInt("shinryoucode");
            String name = rset.getString("name");
            String tensuu = rset.getString("tensuu");
            String tensuu_shikibetsu = rset.getString("tensuu_shikibetsu");
            String shuukeisaki = rset.getString("shuukeisaki");
            String houkatsukensa = rset.getString("houkatsukensa");
            String validFrom = rset.getString("valid_from");
            String validUpto = rset.getString("valid_upto");
            if( printRow ) {
                System.out.printf("%d %s %s %s %s %s %s %s\n", shinryoucode, name, tensuu,
                        tensuu_shikibetsu, shuukeisaki, houkatsukensa, validFrom, validUpto);
            }
            psqlStmt.setInt(1, shinryoucode);
            psqlStmt.setString(2, name);
            psqlStmt.setBigDecimal(3, new BigDecimal(tensuu));
            psqlStmt.setString(4, tensuu_shikibetsu);
            psqlStmt.setString(5, shuukeisaki);
            psqlStmt.setString(6, houkatsukensa);
            psqlStmt.setDate(7, Date.valueOf(validFrom));
            psqlStmt.setDate(8, convertValidUpto(validUpto));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("shinryou_master %d\n", n);
            }
        }
        System.out.printf("shinryou_master %d\n", n);
        rset.close();
        stmt.close();
        psqlStmt.close();
    }
    private  void moveIyakuhinMaster(boolean printRow) throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from iyakuhin_master_arch");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into iyakuhin_master " +
                "(iyakuhincode, name, yomi, unit, yakka, madoku, kouhatsu, zaikei, valid_from, valid_upto) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        while (rset.next()) {
            int iyakuhincode = rset.getInt("iyakuhincode");
            String name = rset.getString("name");
            String yomi = rset.getString("yomi");
            String unit = rset.getString("unit");
            String yakka = rset.getString("yakka");
            String madoku = rset.getString("madoku");
            String kouhatsu = rset.getString("kouhatsu");
            String zaikei = rset.getString("zaikei");
            String validFrom = rset.getString("valid_from");
            String validUpto = rset.getString("valid_upto");
            if( printRow ) {
                System.out.printf("%d %s %s %s %s %s %s %s %s %s\n", iyakuhincode, name, yomi, unit, yakka,
                        madoku, kouhatsu, zaikei, validFrom, validUpto);
            }
            psqlStmt.setInt(1, iyakuhincode);
            psqlStmt.setString(2, name);
            psqlStmt.setString(3, yomi);
            psqlStmt.setString(4, unit);
            psqlStmt.setBigDecimal(5, new BigDecimal(yakka));
            psqlStmt.setString(6, madoku);
            psqlStmt.setString(7, kouhatsu);
            psqlStmt.setString(8, zaikei);
            psqlStmt.setDate(9, Date.valueOf(validFrom));
            psqlStmt.setDate(10, convertValidUpto(validUpto));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("iyakuhin_master %d\n", n);
            }
        }
        System.out.printf("iyakuhin_master %d\n", n);
        rset.close();
        stmt.close();
        psqlStmt.close();
    }

    private static Date convertValidUpto(String sqldate){
        if( sqldate == null ){
            return null;
        } else if( "0000-00-00".equals(sqldate) ){
            return null;
        } else {
            return Date.valueOf(sqldate);
        }
    }

}

