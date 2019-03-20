package jp.chang.myclinic.apitool;

import jp.chang.myclinic.apitool.databasespecifics.DatabaseSpecifics;
import jp.chang.myclinic.apitool.databasespecifics.SqliteSpecifics;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.apitool.lib.tables.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@CommandLine.Command(name = "list-inconsistent-types", description = "Lists.DTO-database type mismatches.")
public class ListInconsistentTypes implements Runnable{

    private Helper helper = Helper.getInstance();

    @Override
    public void run() {
        DatabaseSpecifics dbSpecs = new SqliteSpecifics();
        List<Class<?>> dtoClasses = DtoClassList.getList();
        Supplier<Connection> connProvider = new SqliteConnectionProvider();
        try(Connection conn = connProvider.get()){
            for(Class<?> dtoClass: dtoClasses){
                String tableName = dbSpecs.dtoClassToDbTableName(dtoClass);
                DatabaseMetaData meta = conn.getMetaData();
                List<Helper.ColumnInfo> columns = helper.listColumns(meta, tableName);
                for(Helper.ColumnInfo ci: columns){
                    Class<?> dbColumnClass = dbSpecs.getDbColumnClass(tableName, ci.name, ci.sqlType, ci.dbTypeName);
                    String dtoFieldName = dbSpecs.getDtoFieldName(tableName, ci.name);
                    Class<?> dtoFieldClass = helper.getDTOFieldClass(dtoClass, dtoFieldName);
                    List<String> outputs = new ArrayList<>();
                    if( dbColumnClass != dtoFieldClass ){
                        String s = String.format("  * %s (%s) --- %s (%s)\n", dtoFieldName, dtoFieldClass.getSimpleName(),
                                ci.name, dbColumnClass.getSimpleName());
                        outputs.add(s);
                    }
                    if( outputs.size() > 0 ){
                        System.out.printf("%s - %s\n", dtoClass.getSimpleName(), tableName);
                        outputs.forEach(System.out::println);
                    }
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
