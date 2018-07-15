package jp.chang.myclinic.masterlib;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MainInitMaster {

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(getConnectionString());
        Statement stmt = conn.createStatement();
        Path saveDir = Paths.get("./master-files");
        Path shinryouZip = saveDir.resolve(MasterDownloader.DEFAULT_SHINRYOU_FILENAME);
        String validFrom = LocalDate.now().toString();
        int[] counts = new int[]{0};
        parseZipFile(shinryouZip.toFile(), "s.csv", record -> {
            CSVRow csvRow = new CommonsCSVRow(record);
            ShinryouMasterCSV shinryouCSV = new ShinryouMasterCSV(csvRow);
            int kubun = shinryouCSV.kubun;
            if (kubun == 0 || kubun == 3 || kubun == 5) {
                String sql = shinryouSql(shinryouCSV, validFrom);
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                counts[0] += 1;
            }
        });
        stmt.close();
        conn.close();
        System.out.println(counts[0]);
    }

    private static String getConnectionString(){
        String host = System.getenv("MYCLINIC_DB_HOST");
        String user = System.getenv("MYCLINIC_DB_USER");
        String pass = System.getenv("MYCLINIC_DB_PASS");
        return String.format("jdbc:mysql://%s:3306/myclinic?user=%s&password=%s&zeroDateTimeBehavior=convertToNull&noDatetimeStringSync=true&useUnicode=true&characterEncoding=utf8&verifyServerCertificate=false&useSSL=true",
                host, user, pass);
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

    private static void parseZipFile(File zipFile, String file, Consumer<CSVRecord> consumer) throws IOException {
        try(ZipFile zip = new ZipFile(zipFile, Charset.forName("MS932"))){
            ZipEntry entry = zip.getEntry(file);
            if( entry == null ){
                System.err.println("Cannot find file: " + file);
                System.exit(1);
            } else {
                try(InputStream is = zip.getInputStream(entry)){
                    try (InputStreamReader inputStreamReader = new InputStreamReader(is)) {
                        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(inputStreamReader);
                        for(CSVRecord record: records){
                            consumer.accept(record);
                        }
                    }
                }
            }
        }
    }

}
