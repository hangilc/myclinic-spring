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
                    
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
