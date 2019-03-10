package jp.chang.myclinic.apitool.pgsqltables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Types;

class Column {

    private String name;
    private boolean isPrimary;
    private boolean isAutoIncrement;
    private String type;

    public Column(String name, boolean isAutoIncrement, String type) {
        this.name = name;
        this.isAutoIncrement = isAutoIncrement;
        this.type = type;
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

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", isPrimary=" + isPrimary +
                ", isAutoIncrement=" + isAutoIncrement +
                ", type=" + type +
                '}';
    }
}
