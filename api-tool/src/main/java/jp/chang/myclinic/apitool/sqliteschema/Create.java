package jp.chang.myclinic.apitool.sqliteschema;

import jp.chang.myclinic.apitool.lib.gentablebase.Column;
import jp.chang.myclinic.apitool.lib.gentablebase.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

class Create {

    private Table table;
    private List<TableColumn> columns = new ArrayList<>();
    private int nPrimaries = 0;
    private int nAutoIncs = 0;

    public Create(Table table){
        this.table = table;
        for(Column c: table.getColumns()){
            if( c.isPrimary() ){
                this.nPrimaries += 1;
            }
            if( c.isAutoIncrement() ){
                this.nAutoIncs += 1;
            }
        }
        if( nAutoIncs > 0 && nPrimaries > 1 ){
            System.err.printf("%s has multiple primary keys and also autoinc", table.getName());
            System.exit(1);
        }
        for(Column c: table.getColumns()){
            TableColumn tc = new TableColumn(c, nPrimaries < 2);
            columns.add(tc);
        }
    }

    public String output(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("create table %s (\n", table.getName()));
        List<String> lines = new ArrayList<>();
        columns.stream().map(TableColumn::output).forEach(lines::add);
        if( nPrimaries >= 2 ){
            List<Column> primaries = table.getColumns().stream()
                    .filter(Column::isPrimary).collect(toList());
            String line = String.format("  primary key(%s)", primaries.stream()
                .map(Column::getName).collect(joining(", ")));
            lines.add(line);
        }
        sb.append(String.join(",\n", lines));
        sb.append("\n);\n");
        return sb.toString();
    }

}
