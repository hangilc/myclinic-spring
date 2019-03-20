package jp.chang.myclinic.apitool.lib.tables;

public class Column {

    private String dbColumnName;
    private boolean isAutoIncrement;
    private boolean isPrimary;
    private Class<?> dbColumnClass;
    private String dtoFieldName;

    Column(String dbColumnName, boolean isAutoIncrement, Class<?> dbColumnClass, String dtoFieldName) {
        this.dbColumnName = dbColumnName;
        this.isAutoIncrement = isAutoIncrement;
        this.dbColumnClass = dbColumnClass;
        this.dtoFieldName = dtoFieldName;
    }

    public String getDbColumnName() {
        return dbColumnName;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public Class<?> getDbColumnClass() {
        return dbColumnClass;
    }

    public String getDtoFieldName() {
        return dtoFieldName;
    }
}
