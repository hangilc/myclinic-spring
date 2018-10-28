package jp.chang.myclinic.postgresqldev;

import java.math.BigDecimal;
import java.sql.*;

public class Main {

    private Connection mysqlConn;
    private Connection psqlConn;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        main.moveIyakuhinMaster();
    }

    private Main() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost/myclinic?useSSL=false&zeroDateTimeBehavior=convertToNull",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
        this.psqlConn = DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
    }

    private  void moveIyakuhinMaster() throws Exception {
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
//            System.out.printf("%d %s %s %s %s %s %s %s %s %s\n", iyakuhincode, name, yomi, unit, yakka,
//                    madoku, kouhatsu, zaikei, validFrom, validUpto);
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

