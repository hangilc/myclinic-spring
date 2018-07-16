package jp.chang.myclinic.masterlib;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class MainInitMaster {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(getConnectionString());
        Statement stmt = conn.createStatement();
        MasterHandler handler = new MasterHandler(stmt);
        Path saveDir = Paths.get("./master-files");
        String validFrom = LocalDate.now().toString();
        int count;
        System.out.println("Entering shinryou master.");
        count = enterShinryouMaster(saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME), validFrom, handler);
        System.out.println(count + " shinryou master entered.");
        System.out.println("Entering iyakuhin master.");
        count = enterIyakuhinMaster(saveDir.resolve(MasterDownloader.DEFAULT_IYAKUHIN_FILENAME), validFrom, handler);
        System.out.println(count + " iyakuhin master entered.");
        stmt.close();
        conn.close();
    }

    private static String getConnectionString() {
        String host = System.getenv("MYCLINIC_DB_HOST");
        String user = System.getenv("MYCLINIC_DB_USER");
        String pass = System.getenv("MYCLINIC_DB_PASS");
        return String.format("jdbc:mysql://%s:3306/myclinic?" +
                        "user=%s&password=%s&zeroDateTimeBehavior=convertToNull&" +
                        "noDatetimeStringSync=true&useUnicode=true&" +
                        "characterEncoding=utf8&verifyServerCertificate=false&useSSL=true",
                host, user, pass);
    }

    private static int enterShinryouMaster(Path zipFile, String validFrom, MasterHandler handler) throws IOException {
        class Local {
            private int count;
        }
        Local local = new Local();
        local.count = 0;
        String file = MasterDownloader.SHINRYOU_PREFIX + ".csv";
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShinryouMasterCSV shinryouCSV = new ShinryouMasterCSV(csvRow);
            try {
                if( handler.enterShinryouMaster(shinryouCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

    private static int enterIyakuhinMaster(Path zipFile, String validFrom, MasterHandler handler) throws IOException {
        class Local {
            private int count;
        }
        Local local = new Local();
        local.count = 0;
        String file = MasterDownloader.IYAKUHIN_PREFIX + ".csv";
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            IyakuhinMasterCSV shinryouCSV = new IyakuhinMasterCSV(csvRow);
            try {
                if( handler.enterIyakuhinMaster(shinryouCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

}
