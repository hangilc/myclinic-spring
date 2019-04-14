package jp.chang.myclinic.apitool;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Command(name = "list-shinryou-master", description = "Lists shinryou master")
class ListShinryouMaster implements Runnable {

    @Parameters(paramLabel = "name", arity = "1", description = "Filter text.")
    private String nameFilter;

    @Option(names = {"--index"}, split = ",", description = "Index to select.")
    private List<Integer> indexList;

    @Option(names = {"--output"}, description = "Output format.")
    private String outputFormat = "name";

    @Override
    public void run() {
        Path dbFile = Paths.get(System.getProperty("user.home"), "sqlite-data", "myclinic-test-sqlite.db");
        String atString = LocalDate.now().toString();
        try (Connection conn = new SqliteConnectionProvider(dbFile.toString()).get()) {
            PreparedStatement stmt = conn.prepareStatement("select * from shinryou_master where name like ?" +
                    " and valid_from <= ? and (valid_upto = '0000-00-00' or ? <= valid_upto)");
            stmt.setString(1, "%" + nameFilter + "%");
            stmt.setString(2, atString);
            stmt.setString(3, atString);
            ResultSet rs = stmt.executeQuery();
            List<ShinryouMasterDTO> masters = readMasters(rs);
            rs.close();
            for(int i=0;i<masters.size();i++){
                int index = i+1;
                if( filterIndex(index) ){
                    output(index, masters.get(i));
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean filterIndex(int index){
        if( indexList == null ){
            return true;
        } else {
            return indexList.contains(index);
        }
    }

    private void output(int index, ShinryouMasterDTO master){
        switch(outputFormat){
            case "name": {
                System.out.printf("%2d: %s\n", index, master.name);
                break;
            }
            case "code": {
                outputCode(master);
                break;
            }
            default: {
                System.err.println("Unknown output format: " + outputFormat);
                System.exit(1);
            }
        }
    }

    private void outputCode(ShinryouMasterDTO master){
        System.out.println("ShinryouMasterDTO master = new ShinryouMasterDTO();");
        System.out.printf("master.shinryoucode = %d;\n", master.shinryoucode);
        System.out.printf("master.houkatsukensa = \"%s\";\n", master.houkatsukensa);
        System.out.printf("master.kensaGroup = \"%s\";\n", master.kensaGroup);
        System.out.printf("master.name = \"%s\";\n", master.name);
        System.out.printf("master.oushinkubun = '%c';\n", master.oushinkubun);
        System.out.printf("master.shuukeisaki = \"%s\";\n", master.shuukeisaki);
        System.out.printf("master.tensuu = %d;\n", master.tensuu);
        System.out.printf("master.tensuuShikibetsu = '%c';\n", master.tensuuShikibetsu);
        System.out.printf("master.validFrom = \"%s\";\n", master.validFrom);
        System.out.printf("master.validUpto = \"%s\";\n", master.validUpto);
    }

    private List<ShinryouMasterDTO> readMasters(ResultSet rs) throws SQLException {
        List<ShinryouMasterDTO> result = new ArrayList<>();
        while( rs.next() ){
            ShinryouMasterDTO master = new ShinryouMasterDTO();
            master.shinryoucode = rs.getInt("shinryoucode");
            master.houkatsukensa = rs.getString("houkatsukensa");
            master.kensaGroup = rs.getString("kensa_group");
            master.name = rs.getString("name");
            master.oushinkubun = rs.getString("oushinkubun").charAt(0);
            master.shuukeisaki = rs.getString("shuukeisaki");
            master.tensuu = rs.getInt("tensuu");
            master.tensuuShikibetsu = rs.getString("tensuu_shikibetsu").charAt(0);
            master.validFrom = rs.getString("valid_from");
            master.validUpto = rs.getString("valid_upto");
            result.add(master);
        }
        return result;
    }
}
