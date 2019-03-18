package jp.chang.myclinic.apitool.listdto;

import jp.chang.myclinic.apitool.PgsqlConnectionProvider;
import jp.chang.myclinic.apitool.databasespecifics.PgsqlSpecifics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@CommandLine.Command(name = "--list-dto")
public class ListDTO implements Runnable {


    @Override
    public void run() {
        try(Connection conn = PgsqlConnectionProvider.get()){
            DatabaseMetaData meta = conn.getMetaData();
            PgsqlSpecifics dbSpecs = new PgsqlSpecifics();
            for(String table: listTableNames(meta)){
                Class<?> cls = dbSpecs.mapTableNameToDtoClass(table);
                System.out.println(cls.getSimpleName());
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private List<String> listTableNames(DatabaseMetaData meta) throws SQLException {
        ResultSet rs = meta.getTables(null, "public", "%", new String[]{"TABLE"});
        List<String> tables = new ArrayList<>();
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        rs.close();
        return tables;
    }


}
