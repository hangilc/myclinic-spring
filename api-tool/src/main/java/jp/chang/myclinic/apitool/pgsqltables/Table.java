package jp.chang.myclinic.apitool.pgsqltables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Table {

    private String name;
    private List<Column> columns;

    public Table(String name, List<Column> columns) {
        this.name = name;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumns() {
        return columns;
    }
}
