package jp.chang.myclinic.apitool.databasespecifics;

import java.util.Map;

public interface DatabaseSpecifics {

    String dtoClassToDbTableName(Class<?> dtoClass);
    String getDtoFieldName(String table, String dbColumnName);
    Class<?> getDbColumnClass(String tableName, String columnName, int sqlType, String dbTypeName);

}
