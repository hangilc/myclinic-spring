package jp.chang.myclinic.apitool.lib.tables;

import java.nio.file.Path;

public interface Config {

    String basePackage();
    Path baseDir();
    String dtoClassToDbTableName(Class<?> dtoClass);
    Class<?> getDbColumnClass(int sqlType, String dbTypeName);
    String getDtoFieldName(String table, String dbColumnName);
}
