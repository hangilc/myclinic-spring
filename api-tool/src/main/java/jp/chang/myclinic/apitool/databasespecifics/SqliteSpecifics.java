package jp.chang.myclinic.apitool.databasespecifics;

import jp.chang.myclinic.apitool.lib.Helper;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SqliteSpecifics implements DatabaseSpecifics {

    private Helper helper = Helper.getInstance();

    @Override
    public String getCatalog() {
        return null;
    }

    @Override
    public String getSchema() {
        return null;
    }

    @Override
    public String dtoClassToDbTableName(Class<?> dtoClass) {
        String dtoBaseName = dtoClass.getSimpleName().replaceAll("DTO$", "");
        return helper.toSnake(dtoBaseName);
    }

    @Override
    public Class<?> getDbColumnClass(String tableName, String columnName, int sqlType, String dbTypeName) {
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
