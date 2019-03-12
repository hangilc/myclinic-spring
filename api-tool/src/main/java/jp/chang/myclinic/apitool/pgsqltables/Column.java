package jp.chang.myclinic.apitool.pgsqltables;

class Column {

    private String name;
    private boolean isPrimary;
    private boolean isAutoIncrement;
    private Class<?> jdbcType;
    private String dtoField;

    public Column(String name, boolean isAutoIncrement, Class<?> jdbcType, String dtoField) {
        this.name = name;
        this.isAutoIncrement = isAutoIncrement;
        this.jdbcType = jdbcType;
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

    public Class<?> getJdbcType() {
        return jdbcType;
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
                ", jdbcType=" + jdbcType +
                ", dtoField='" + dtoField + '\'' +
                '}';
    }
}
