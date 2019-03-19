package jp.chang.myclinic.backenddb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlTranslator {

    public interface TableInfo {
        String getDtoName();
        String getDbTableName();
        Map<String, String> getDtoFieldToDbColumnMap();
    }

    public class AliasedTable {
        public TableInfo table;
        public String alias;

        public AliasedTable(TableInfo table, String alias) {
            this.table = table;
            this.alias = alias;
        }
    }

    public String translate(String src, TableInfo table){
        return translate(src, List.of(new AliasedTable(table, "")));
    }

    public String translate(String src, TableInfo table, String alias){
        return translate(src, List.of(new AliasedTable(table, alias)));
    }

    public String translate(String src, TableInfo table1, String alias1, TableInfo table2, String alias2){
        return translate(src, List.of(new AliasedTable(table1, alias1), new AliasedTable(table2, alias2)));
    }

    public String translate(String src, TableInfo table1, String alias1, TableInfo table2, String alias2,
                            TableInfo table3, String alias3){
        return translate(src, List.of(
                new AliasedTable(table1, alias1),
                new AliasedTable(table2, alias2),
                new AliasedTable(table3, alias3)
        ));
    }

    public String translate(String src, List<AliasedTable> tables){
        Map<String, String> rewriteMap = new HashMap<>();
        for(AliasedTable at: tables){
            TableInfo table = at.table;
            String alias = at.alias;
            rewriteMap.put(table.getDtoName(), table.getDbTableName());
            for(Map.Entry<String, String> entry: table.getDtoFieldToDbColumnMap().entrySet()){
                String key = entry.getKey();
                String val = entry.getValue();
                if( alias != null && !alias.isEmpty() ){
                    key = alias + "." + key;
                    val = alias + "." + val;
                }
                rewriteMap.put(key, val);
            }
        }
        Pattern pat = Pattern.compile("\\b(" + String.join("|", rewriteMap.keySet()) + ")\\b");
        StringBuilder sb = new StringBuilder();
        Matcher matcher = pat.matcher(src);
        while( matcher.find() ){
            String g = matcher.group(1);
            matcher.appendReplacement(sb, rewriteMap.get(g));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
