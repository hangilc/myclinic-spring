package jp.chang.myclinic.apitool;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import picocli.CommandLine;
import picocli.CommandLine.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Command(name = "resolve-shinryoucode-by-name")
public class ResolveShinryoucodeByName implements Runnable {

    @Option(names = {"--at"}, description = "Search shinryoumaster which is valid at.")
    private String at = LocalDate.now().toString();

    @Override
    public void run() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Path mapFile = Paths.get("config", "shinryou-names.yml");
        Path dbFile = Paths.get(System.getProperty("user.home"), "sqlite-data", "myclinic-test-sqlite.db");
        try(Connection conn = new SqliteConnectionProvider(dbFile.toString()).get()) {
            PreparedStatement stmt = conn.prepareStatement("select shinryoucode from shinryou_master" +
                    " where name = ? and valid_from <= ? and (valid_upto = '0000-00-00' or ? <= valid_upto)");
            Map<String, List<String>> nameCadidates = mapper.readValue(mapFile.toFile(),
                    new TypeReference<LinkedHashMap<String, List<String>>>() {
                    });
            for(String key: nameCadidates.keySet()){
                List<String> nameList = nameCadidates.get(key);
                int shinryoucode = 0;
                for(String name: nameList){
                    stmt.setString(1, name);
                    stmt.setString(2, at);
                    stmt.setString(3, at);
                    ResultSet rs = stmt.executeQuery();
                    if( rs.next() ){
                        shinryoucode = rs.getInt(1);
                    }
                    rs.close();
                }
                System.out.printf("%s: %d\n", key, shinryoucode);
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }



}
