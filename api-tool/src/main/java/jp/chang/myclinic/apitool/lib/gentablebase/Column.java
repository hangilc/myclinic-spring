package jp.chang.myclinic.apitool.lib.gentablebase;

public class Column {

    private String name;
    private boolean isPrimary;
    private boolean isAutoIncrement;
    private Class<?> dbType;
    private String dtoField;

    public Column(String name, boolean isAutoIncrement, Class<?> dbType, String dtoField) {
        this.name = name;
        this.isAutoIncrement = isAutoIncrement;
        this.dbType = dbType;
        this.dtoField = dtoField;
    }

    public String getName() {
        return name;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public Class<?> getDbType() {
        return dbType;
    }

    public String getDtoField() {
        return dtoField;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", isPrimary=" + isPrimary +
                ", isAutoIncrement=" + isAutoIncrement +
                ", dbType=" + dbType +
                ", dtoField='" + dtoField + '\'' +
                '}';
    }
}
