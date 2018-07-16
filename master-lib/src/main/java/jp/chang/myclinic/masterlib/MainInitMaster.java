package jp.chang.myclinic.masterlib;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.regex.Pattern;

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
        System.out.println("Entering kizai master.");
        count = enterKizaiMaster(saveDir.resolve(MasterDownloader.DEFAULT_KIZAI_FILENAME), validFrom, handler);
        System.out.println(count + " kizai master entered.");
        System.out.println("Entering shoubyoumei master.");
        count = enterShoubyoumeiMaster(saveDir.resolve(MasterDownloader.DEFAULT_SHOUBYOUMEI_FILENAME), validFrom, handler);
        System.out.println(count + " shoubyoumei master entered.");
        System.out.println("Entering shuushokugo master.");
        count = enterShuushokugoMaster(saveDir.resolve(MasterDownloader.DEFAULT_SHUUSHOKUGO_FILENAME), validFrom, handler);
        System.out.println(count + " shuushokugo master entered.");
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
        Pattern file = Pattern.compile(MasterDownloader.SHINRYOU_PREFIX + "\\.csv");
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShinryouMasterCSV shinryouCSV = new ShinryouMasterCSV(csvRow);
            try {
                if( handler.enterShinryouMaster(shinryouCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                System.err.println("line: " + shinryouCSV.name);
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
        Pattern file = Pattern.compile(MasterDownloader.IYAKUHIN_PREFIX + "\\.csv");
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            IyakuhinMasterCSV iyakuhinCSV = new IyakuhinMasterCSV(csvRow);
            try {
                if( handler.enterIyakuhinMaster(iyakuhinCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

    private static int enterKizaiMaster(Path zipFile, String validFrom, MasterHandler handler) throws IOException {
        class Local {
            private int count;
        }
        Local local = new Local();
        local.count = 0;
        Pattern file = Pattern.compile(MasterDownloader.KIZAI_PREFIX + "\\.csv");
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            KizaiMasterCSV kizaiCSV = new KizaiMasterCSV(csvRow);
            try {
                if( handler.enterKizaiMaster(kizaiCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

    private static int enterShoubyoumeiMaster(Path zipFile, String validFrom, MasterHandler handler) throws IOException {
        class Local {
            private int count;
        }
        Local local = new Local();
        local.count = 0;
        Pattern file = Pattern.compile(MasterDownloader.SHOUBYOUMEI_PREFIX + ".*\\.txt");
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShoubyoumeiMasterCSV shoubyoumeiCSV = new ShoubyoumeiMasterCSV(csvRow);
            try {
                if( handler.enterShoubyoumeiMaster(shoubyoumeiCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

    private static int enterShuushokugoMaster(Path zipFile, String validFrom, MasterHandler handler) throws IOException {
        class Local {
            private int count;
        }
        Local local = new Local();
        local.count = 0;
        Pattern file = Pattern.compile(MasterDownloader.SHUUSHOKUGO_PREFIX + ".*\\.txt");
        ZipFileParser.parse(zipFile.toFile(), file, record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShuushokugoMasterCSV shuushokugoCSV = new ShuushokugoMasterCSV(csvRow);
            try {
                if( handler.enterShuushokugoMaster(shuushokugoCSV, validFrom) ){
                    local.count += 1;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        return local.count;
    }

}
