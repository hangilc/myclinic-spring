package jp.chang.myclinic.postgresqldev;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {

    private Connection mysqlConn;
    private Connection psqlConn;

    public static void main(String[] args) throws Exception {
        Main main = new Main();
        //main.moveIyakuhinMaster(false);
        //main.moveShinryouMaster(false);
        //main.moveKizaiMaster(false);
        //main.movePatient();
        //main.movePracticeLog();
        //main.moveShahokokuho();
        //main.moveRoujin();
        //main.moveKoukikourei();
        //main.moveKouhi();
        //main.moveVisit();
        //main.moveWqueue();
        //main.movePharmaQueue();
        //main.moveText();
        //main.moveDrug();
        //main.moveShinryou();
        //main.moveConduct();
        //main.moveGazouLabel();
        main.moveConductDrug();
    }

    private Main() throws Exception {
        Class.forName("org.postgresql.Driver");
        this.mysqlConn = DriverManager.getConnection("jdbc:mysql://localhost/myclinic?useSSL=false&zeroDateTimeBehavior=convertToNull",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
        this.psqlConn = DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
    }

    private Mover createMover(String sourceTable, String targetTable){
        return new Mover(mysqlConn, psqlConn, sourceTable, targetTable);
    }

    private Mover createMover(String table){
        return createMover(table, table);
    }

    private void moveConductDrug() throws Exception {
        Mover mover = createMover("visit_conduct_drug", "conduct_drug");
        mover.addSerialColumn("id", "conduct_drug_id");
        mover.addIntColumn("visit_conduct_id", "conduct_id");
        mover.addIntColumn("iyakuhincode");
        mover.addDecimalColumn("amount");
        mover.move();
    }

    private void moveGazouLabel() throws Exception {
        Mover mover = createMover("visit_gazou_label", "gazou_label");
        mover.addIntColumn("visit_conduct_id", "conduct_id");
        mover.addStringColumn("label");
        mover.move();
    }

    private void moveConduct() throws Exception {
        Mover mover = createMover("visit_conduct", "conduct");
        mover.addSerialColumn("id", "conduct_id");
        mover.addIntColumn("visit_id");
        mover.addIntColumn("kind");
        mover.move();
    }

    private void moveShinryou() throws Exception {
        Mover mover = createMover("visit_shinryou", "shinryou");
        mover.addSerialColumn("shinryou_id");
        mover.addIntColumn("visit_id");
        mover.addIntColumn("shinryoucode");
        mover.move();
    }

    private void moveDrug() throws Exception {
        Mover mover = createMover("visit_drug", "drug");
        mover.addSerialColumn("drug_id");
        mover.addIntColumn("visit_id");
        mover.addIntColumn("d_iyakuhincode", "iyakuhincode");
        mover.addDecimalColumn("d_amount", "amount");
        mover.addStringColumn("d_usage", "usage");
        mover.addIntColumn("d_days", "days");
        mover.addIntColumn("d_category", "category");
        mover.addIntColumn("d_prescribed", "prescribed");
        mover.move();
    }

    private void moveText() throws Exception {
        Mover mover = createMover("visit_text", "text");
        mover.addSerialColumn("text_id");
        mover.addIntColumn("visit_id");
        mover.addStringColumn("content");
        mover.move();
    }

    private void movePharmaQueue() throws Exception {
        Mover mover = createMover("pharma_queue");
        mover.addIntColumn("visit_id");
        mover.addIntColumn("pharma_state");
        mover.move();
    }

    private void moveWqueue() throws Exception {
        Mover mover = createMover("wqueue");
        mover.addIntColumn("visit_id");
        mover.addIntColumn("wait_state");
        mover.move();
    }

    private void moveVisit() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from visit");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into visit " +
                "(visit_id, patient_id, visited_at, shahokokuho_id, roujin_id, koukikourei_id, " +
                " kouhi_1_id, kouhi_2_id, kouhi_3_id) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("visit_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setInt(2, rset.getInt("patient_id"));
            psqlStmt.setTimestamp(3, rset.getTimestamp("v_datetime"));
            psqlStmt.setObject(4, zeroToNull(rset.getInt("shahokokuho_id")));
            psqlStmt.setObject(5, zeroToNull(rset.getInt("roujin_id")));
            psqlStmt.setObject(6, zeroToNull(rset.getInt("koukikourei_id")));
            psqlStmt.setObject(7, zeroToNull(rset.getInt("kouhi_1_id")));
            psqlStmt.setObject(8, zeroToNull(rset.getInt("kouhi_2_id")));
            psqlStmt.setObject(9, zeroToNull(rset.getInt("kouhi_3_id")));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("visit %d\n", n);
            }
        }
        System.out.printf("visit %d\n", n);
        System.out.printf("NEXT VISIT_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table visit alter column visit_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("VISIT_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void moveKouhi() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from kouhi");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into kouhi " +
                "(kouhi_id, patient_id, futansha, jukyuusha, " +
                " valid_from, valid_upto) " +
                        "values (?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("kouhi_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setInt(2, rset.getInt("patient_id"));
            psqlStmt.setInt(3, rset.getInt("futansha"));
            psqlStmt.setInt(4, rset.getInt("jukyuusha"));
            {
                LocalDate validFrom = LocalDate.parse(rset.getString("valid_from"));
                LocalDate validFromOrig = validFrom;
                LocalDate validUpto;
                String validUptoSqldate = rset.getString("valid_upto");
                if( validUptoSqldate != null && !"0000-00-00".equals(validUptoSqldate) ){
                    validUpto = LocalDate.parse(validUptoSqldate);
                    if( validUpto.isBefore(validFrom) ){
                        validFrom = validUpto.minus(1, ChronoUnit.YEARS);
                        System.err.printf("kouhi fix: %d valid_from %s -> %s\n",
                                id, validFromOrig.toString(), validFrom.toString());
                    }
                } else {
                    validUpto = null;
                }
                psqlStmt.setObject(5, validFrom);
                psqlStmt.setObject(6, validUpto);
            }
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("kouhi %d\n", n);
            }
        }
        System.out.printf("kouhi %d\n", n);
        System.out.printf("NEXT KOUHI_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table kouhi alter column kouhi_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("KOUHI_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void moveKoukikourei() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from hoken_koukikourei");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into koukikourei " +
                "(koukikourei_id, patient_id, hokensha_bangou, hihokensha_bangou, futan_wari, " +
                " valid_from, valid_upto) " +
                        "values (?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("koukikourei_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setInt(2, rset.getInt("patient_id"));
            psqlStmt.setInt(3, Integer.parseInt(rset.getString("hokensha_bangou")));
            psqlStmt.setInt(4, Integer.parseInt(rset.getString("hihokensha_bangou")));
            psqlStmt.setInt(5, rset.getInt("futan_wari"));
            {
                LocalDate validFrom = LocalDate.parse(rset.getString("valid_from"));
                LocalDate validFromOrig = validFrom;
                LocalDate validUpto;
                String validUptoSqldate = rset.getString("valid_upto");
                if( validUptoSqldate != null && !"0000-00-00".equals(validUptoSqldate) ){
                    validUpto = LocalDate.parse(validUptoSqldate);
                    if( validUpto.isBefore(validFrom) ){
                        validFrom = validUpto.minus(1, ChronoUnit.YEARS);
                        System.err.printf("koukikourei fix: %d valid_from %s -> %s\n",
                                id, validFromOrig.toString(), validFrom.toString());
                    }
                } else {
                    validUpto = null;
                }
                psqlStmt.setObject(6, validFrom);
                psqlStmt.setObject(7, validUpto);
            }
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("koukikourei %d\n", n);
            }
        }
        System.out.printf("koukikourei %d\n", n);
        System.out.printf("NEXT KOUKIKOUREI_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table koukikourei alter column koukikourei_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("KOUKIKOUREI_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void moveRoujin() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from hoken_roujin");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into roujin " +
                "(roujin_id, patient_id, shichouson, jukyuusha, futan_wari, " +
                " valid_from, valid_upto) " +
                        "values (?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("roujin_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setInt(2, rset.getInt("patient_id"));
            psqlStmt.setInt(3, rset.getInt("shichouson"));
            psqlStmt.setInt(4, rset.getInt("jukyuusha"));
            psqlStmt.setInt(5, rset.getInt("futan_wari"));
            {
                LocalDate validFrom = LocalDate.parse(rset.getString("valid_from"));
                LocalDate validFromOrig = validFrom;
                LocalDate validUpto;
                String validUptoSqldate = rset.getString("valid_upto");
                if( validUptoSqldate != null && !"0000-00-00".equals(validUptoSqldate) ){
                    validUpto = LocalDate.parse(validUptoSqldate);
                    if( validUpto.isBefore(validFrom) ){
                        validFrom = validUpto.minus(1, ChronoUnit.YEARS);
                        System.err.printf("roujin fix: %d valid_from %s -> %s\n",
                                id, validFromOrig.toString(), validFrom.toString());
                    }
                } else {
                    validUpto = null;
                }
                psqlStmt.setObject(6, validFrom);
                psqlStmt.setObject(7, validUpto);
            }
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("roujin %d\n", n);
            }
        }
        System.out.printf("roujin %d\n", n);
        System.out.printf("NEXT ROUJIN_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table roujin alter column roujin_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("ROUJIN_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void moveShahokokuho() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from hoken_shahokokuho");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into shahokokuho " +
                "(shahokokuho_id, patient_id, hokensha_bangou, hihokensha_kigou, hihokensha_bangou, honnin, " +
                " valid_from, valid_upto, kourei) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("shahokokuho_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setInt(2, rset.getInt("patient_id"));
            psqlStmt.setInt(3, rset.getInt("hokensha_bangou"));
            psqlStmt.setString(4, rset.getString("hihokensha_kigou"));
            psqlStmt.setString(5, rset.getString("hihokensha_bangou"));
            psqlStmt.setInt(6, rset.getInt("honnin"));
            {
                LocalDate validFrom = LocalDate.parse(rset.getString("valid_from"));
                LocalDate validFromOrig = validFrom;
                LocalDate validUpto;
                String validUptoSqldate = rset.getString("valid_upto");
                if( validUptoSqldate != null && !"0000-00-00".equals(validUptoSqldate) ){
                    validUpto = LocalDate.parse(validUptoSqldate);
                    if( validUpto.isBefore(validFrom) ){
                        validFrom = validUpto.minus(1, ChronoUnit.YEARS);
                        System.err.printf("shahokokuho fix: %d valid_from %s -> %s\n",
                                id, validFromOrig.toString(), validFrom.toString());
                    }
                } else {
                    validUpto = null;
                }
                psqlStmt.setObject(7, validFrom);
                psqlStmt.setObject(8, validUpto);
            }
            psqlStmt.setInt(9, rset.getInt("kourei"));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("shahokokuho %d\n", n);
            }
        }
        System.out.printf("shahokokuho %d\n", n);
        System.out.printf("NEXT SHAHOKOKUHO_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table shahokokuho alter column shahokokuho_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("SHAHOKOKUHO_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void movePracticeLog() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from practice_log");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into practice_log " +
                "(practice_log_id, created_at, kind, body) " +
                        "values (?, ?, ?, ?::json)");
        int n = 0;
        int maxId = 0;
        while (rset.next()) {
            int id = rset.getInt("practice_log_id");
            if( maxId < id ){
                maxId = id;
            }
            psqlStmt.setInt(1, id);
            psqlStmt.setTimestamp(2, rset.getTimestamp("created_at"));
            psqlStmt.setString(3, rset.getString("kind"));
            psqlStmt.setString(4, rset.getString("body"));
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("practice_log %d\n", n);
            }
        }
        System.out.printf("practice_log %d\n", n);
        System.out.printf("NEXT PRACTICE_LOG_ID: %d\n", maxId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table practice_log alter column practice_log_id restart with %d",
                    maxId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("PRACTICE_LOG_ID SEQUENCE RESTARTS WITH " + (maxId + 1));
        }
    }

    private void movePatient() throws Exception {
        Statement stmt = mysqlConn.createStatement();
        ResultSet rset = stmt.executeQuery("select * from patient");
        PreparedStatement psqlStmt = psqlConn.prepareStatement("insert into patient " +
                "(patient_id, last_name, first_name, last_name_yomi, first_name_yomi, " +
                " sex, birthday, address, phone) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        int maxPatientId = 0;
        while (rset.next()) {
            int patientId = rset.getInt("patient_id");
            String lastName = rset.getString("last_name");
            String firstName = rset.getString("first_name");
            String lastNameYomi = rset.getString("last_name_yomi");
            String firstNameYomi = rset.getString("first_name_yomi");
            String sex = rset.getString("sex");
            String birthday = rset.getString("birth_day");
            String address = rset.getString("address");
            String phone = rset.getString("phone");
            if( patientId > maxPatientId ){
                maxPatientId = patientId;
            }
            psqlStmt.setInt(1, patientId);
            psqlStmt.setString(2, lastName);
            psqlStmt.setString(3, firstName);
            psqlStmt.setString(4, lastNameYomi);
            psqlStmt.setString(5, firstNameYomi);
            psqlStmt.setString(6, sex);
            psqlStmt.setObject(7, LocalDate.parse(birthday));
            psqlStmt.setString(8, address);
            psqlStmt.setString(9, phone);
            psqlStmt.executeUpdate();
            n += 1;
            if( n % 1000 == 0 ){
                System.out.printf("patient %d\n", n);
            }
        }
        System.out.printf("patient %d\n", n);
        System.out.printf("NEXT PATIENT_ID: %d\n", maxPatientId + 1);
        psqlStmt.close();
        rset.close();
        stmt.close();
        {
            String sql = String.format("alter table patient alter column patient_id restart with %d",
                    maxPatientId + 1);
            Statement seqStmt = psqlConn.createStatement();
            seqStmt.executeUpdate(sql);
            seqStmt.close();
            System.out.println("PATIENT_ID SEQUENCE RESTARTS WITH " + (maxPatientId + 1));
        }
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
                "(shinryoucode, name, tensuu, tensuu_shikibetsu, shuukeisaki, houkatsukensa, " +
                "kensagroup, valid_from, valid_upto) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int n = 0;
        while (rset.next()) {
            int shinryoucode = rset.getInt("shinryoucode");
            String name = rset.getString("name");
            String tensuu = rset.getString("tensuu");
            String tensuu_shikibetsu = rset.getString("tensuu_shikibetsu");
            String shuukeisaki = rset.getString("shuukeisaki");
            String houkatsukensa = rset.getString("houkatsukensa");
            String kensagroup = rset.getString("kensagroup");
            String validFrom = rset.getString("valid_from");
            String validUpto = rset.getString("valid_upto");
            if( printRow ) {
                System.out.printf("%d %s %s %s %s %s %s %s %s\n", shinryoucode, name, tensuu,
                        tensuu_shikibetsu, shuukeisaki, houkatsukensa, kensagroup, validFrom, validUpto);
            }
            psqlStmt.setInt(1, shinryoucode);
            psqlStmt.setString(2, name);
            psqlStmt.setBigDecimal(3, new BigDecimal(tensuu));
            psqlStmt.setString(4, tensuu_shikibetsu);
            psqlStmt.setString(5, shuukeisaki);
            psqlStmt.setString(6, houkatsukensa);
            psqlStmt.setString(7, kensagroup);
            psqlStmt.setDate(8, Date.valueOf(validFrom));
            psqlStmt.setDate(9, convertValidUpto(validUpto));
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

    private static Integer zeroToNull(Integer value){
        if( value == null || value == 0 ){
            return null;
        } else {
            return value;
        }
    }

}

