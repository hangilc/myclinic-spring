package jp.chang.myclinic.apitool.lib.sqliteschema;

import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class Create {

    private String tableName;
    private List<TableColumn> columns = new ArrayList<>();
    private int nPrimaries = 0;

    public Create(String tableName) {
        this.tableName = tableName;
    }

    public void addColumn(String name, String type, boolean isPrimary, boolean isAutoInc, String dtoFieldName){
        columns.add(new TableColumn(name, type, isPrimary, isAutoInc, dtoFieldName));
        if( isPrimary ){
            nPrimaries += 1;
        }
    }

    public String output() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("create table %s (\n", tableName));
        List<String> lines = new ArrayList<>();
        boolean printPrimary = nPrimaries < 2;
        columns.stream().map(c -> c.output(printPrimary)).forEach(lines::add);
        if (nPrimaries >= 2) {
            List<String> primaries = columns.stream()
                    .filter(TableColumn::isPrimary).map(TableColumn::getName).collect(toList());
            String line = String.format("  primary key(%s)", String.join(", ", primaries));
            lines.add(line);
        }
        sb.append(String.join(",\n", lines));
        sb.append("\n);\n");
        return sb.toString();
    }

    public String getTableName() {
        return tableName;
    }

    public List<TableColumn> getColumns() {
        return columns;
    }
}
