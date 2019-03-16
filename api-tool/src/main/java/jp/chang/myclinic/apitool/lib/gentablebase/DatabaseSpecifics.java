package jp.chang.myclinic.apitool.lib.gentablebase;

import java.util.Map;

public interface DatabaseSpecifics {

    Class<?> mapDatabaseClass(int sqlType, String dbTypeName);
    String resolveDtoFieldName(String table, String dbColumnName);
    Class<?> mapTableNameToDtoClass(String tableName);

}
