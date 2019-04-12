package jp.chang.myclinic.apitool;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jp.chang.myclinic.dto.PrescExampleDTO;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Command(name = "list-presc-example")
public class ListPrescExample implements Runnable {

    @Option(names = {"--output"}, description = "Output form")
    private String output = "short";

    @Option(names = {"--name"}, description = "Filters by name")
    private String nameFilter;

    @Option(names = {"--index"}, split = ",", description = "Filters by index")
    private int[] indexList;

    private ObjectMapper mapper = new ObjectMapper();
    {
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public class Item {
        public PrescExampleDTO example;
        public String name;
    }

    @Override
    public void run() {
        Path dbFile = Paths.get(System.getProperty("user.home"), "sqlite-data", "myclinic-test-sqlite.db");
        try (Connection conn = new SqliteConnectionProvider(dbFile.toString()).get()) {
            List<Item> examples = readPrescExamples(conn);
            for(int i=0;i<examples.size();i++){
                Item item = examples.get(i);
                if( filterByName(item.name) && filterByIndex(i+1) ) {
                    outputItem(i + 1, item);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private boolean filterByName(String name){
        if( name == null ){
            return true;
        } else {
            return name.contains(nameFilter);
        }
    }

    private boolean filterByIndex(int index){
        if( indexList == null ){
            return true;
        } else {
            for (int value : indexList) {
                if (index == value) {
                    return true;
                }
            }
            return false;
        }
    }

    private void outputCode(Item item){
        System.out.printf("// %s\n", item.name);
        System.out.printf("example.prescExampleId = %d;\n", item.example.prescExampleId);
        System.out.printf("example.iyakuhincode = %d;\n", item.example.iyakuhincode);
        System.out.printf("example.amount = \"%s\";\n", item.example.amount);
        System.out.printf("example.category = %d;\n", item.example.category);
        System.out.printf("example.comment = \"%s\";\n", item.example.comment);
        System.out.printf("example.days = %d;\n", item.example.days);
        System.out.printf("example.masterValidFrom = \"%s\";\n", item.example.masterValidFrom);
        System.out.printf("example.usage = \"%s\";\n", item.example.usage);
        System.out.println();
    }

    private void outputItem(int index, Item item){
        switch(output){
            case "short": {
                System.out.printf("%3d: %s %s\n", index, item.name, item.example.usage);
                break;
            }
            case "code": {
                outputCode(item);
                break;
            }
            default: {
                System.err.println("Unknown output form: " + output);
                System.exit(1);
            }
        }
    }

    private List<Item> readPrescExamples(Connection conn) throws SQLException {
        List<Item> result = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select e.*, m.name from presc_example e, iyakuhin_master m" +
                " where e.iyakuhincode = m.iyakuhincode and e.master_valid_from = m.valid_from" +
                " order by presc_example_id");
        while(rs.next()){
            PrescExampleDTO example = new PrescExampleDTO();
            example.prescExampleId = rs.getInt("presc_example_id");
            example.iyakuhincode = rs.getInt("iyakuhincode");
            example.amount = rs.getString("amount");
            example.category = rs.getInt("category");
            example.comment = rs.getString("comment");
            example.days = rs.getInt("days");
            example.masterValidFrom = rs.getString("master_valid_from");
            example.usage = rs.getString("usage");
            Item item = new Item();
            item.example = example;
            item.name = rs.getString("name");
            result.add(item);
        }
        return result;
    }
}
