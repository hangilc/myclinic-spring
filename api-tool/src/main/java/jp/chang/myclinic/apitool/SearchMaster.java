package jp.chang.myclinic.apitool;

import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Command(name = "search-master", description = "Searches in master database.")
class SearchMaster implements Runnable {

    @Option(names = {"--index"}, description = "Select index in the result set.")
    Integer selectIndex = null;

    @Option(names = {"--output"}, description = "Specifies output format.")
    String output = "name";

    @Parameters(paramLabel = "search-text", arity = "1")
    private String searchText;

    @Override
    public void run() {
        try(Connection conn = getConnection()){
            if( !searchText.contains("%") ){
                searchText = "%" + searchText + "%";
            }
            doSearch(conn, searchText);
        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }

    private void doSearch(Connection conn, String searchText) throws SQLException {
        String atString = LocalDate.now().toString();
        String sql = "select * from iyakuhin_master where name like ? " +
                " and valid_from <= ? and (valid_upto = '0000-00-00' or valid_upto >= ?) ";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, searchText);
        stmt.setString(2, atString);
        stmt.setString(3, atString);
        ResultSet rs = stmt.executeQuery();
        int index = 1;
        while( rs.next() ){
            IyakuhinMasterDTO master = new IyakuhinMasterDTO();
            master.iyakuhincode = rs.getInt("iyakuhincode");
            master.name = rs.getString("name");
            master.yomi = rs.getString("yomi");
            master.yakka = rs.getDouble("yakka");
            master.unit = rs.getString("unit");
            master.kouhatsu = rs.getString("kouhatsu").charAt(0);
            master.madoku = rs.getString("madoku").charAt(0);
            master.zaikei = rs.getString("zaikei").charAt(0);
            master.validFrom = rs.getString("valid_from");
            master.validUpto = rs.getString("valid_upto");
            int rowIndex = index++;
            if( selectIndex != null && selectIndex != rowIndex ){
                continue;
            }
            switch(output){
                case "data": {
                    System.out.println(master);
                    break;
                }
                case "code": {
                    outputIyakuhinMasterAsCode(master);
                    break;
                }
                case "name": {
                    System.out.printf("%2d: %s\n", rowIndex, master.name);
                    break;
                }
                default: {
                    System.err.println("Unknown output format: " + output);
                    System.exit(1);
                }
            }
        }
        stmt.close();
    }

    private void outputIyakuhinMasterAsCode(IyakuhinMasterDTO master){
        String varName = "master";
        System.out.printf("IyakuhinMasterDTO %s = new IyakuhinMasterDTO();\n", varName);
        System.out.printf("%s.iyakuhincode = %d;\n", varName, master.iyakuhincode);
        System.out.printf("%s.name = \"%s\";\n", varName, master.name);
        System.out.printf("%s.yomi = \"%s\";\n", varName, master.yomi);
        System.out.printf("%s.yakka = %f;\n", varName, master.yakka);
        System.out.printf("%s.unit = \"%s\";\n", varName, master.unit);
        System.out.printf("%s.kouhatsu = '%c';\n", varName, master.kouhatsu);
        System.out.printf("%s.madoku = '%c';\n", varName, master.madoku);
        System.out.printf("%s.zaikei = '%c';\n", varName, master.zaikei);
        System.out.printf("%s.validFrom = \"%s\";\n", varName, master.validFrom);
        System.out.printf("%s.validUpto = \"%s\";\n", varName, master.validUpto);
    }

    private Connection getConnection(){
        Path path = Paths.get(System.getProperty("user.home"), "sqlite-data", "myclinic-statics-sqlite.db");
        return new SqliteConnectionProvider(path.toString()).get();
    }

}