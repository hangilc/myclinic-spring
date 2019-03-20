package jp.chang.myclinic.apitool.lib.tables;

import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class SqliteConfig implements Config {

    private Helper helper = Helper.getInstance();

    @Override
    public String basePackage() {
        return "jp.chang.myclinic.backendsqlite";
    }

    @Override
    public Path baseDir() {
        return Paths.get("backend-sqlite/src/main/java/jp/chang/myclinic/backendsqlite");
    }

    @Override
    public String dtoClassToDbTableName(Class<?> dtoClass) {
        String dtoBaseName = dtoClass.getSimpleName().replaceAll("DTO$", "");
        return helper.toSnake(dtoBaseName);
    }

    @Override
    public Class<?> getDbColumnClass(int sqlType, String dbTypeName) {
        switch (dbTypeName) {
            case "INTEGER":
                return Integer.class;
            case "REAL":
                return Double.class;
            default:
                return String.class;
        }
    }

    @Override
    public String getDtoFieldName(String table, String dbColumnName) {
        return helper.snakeToCamel(dbColumnName);
    }
}
