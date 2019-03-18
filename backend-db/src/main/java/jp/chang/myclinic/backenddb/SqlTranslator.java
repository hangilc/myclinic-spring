package jp.chang.myclinic.backenddb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    public String translate(String src, TableInfo table){
        Map<String, String> dtoToDbMap = table.getDtoFieldToDbColumnMap();
        StringBuilder sb = new StringBuilder();
        List<String> needles = new ArrayList<>();
        needles.add(table.getDtoName());
        needles.addAll(dtoToDbMap.keySet());
        Pattern pat = Pattern.compile(String.join("|", needles));
        System.err.println(pat);
        Matcher matcher = pat.matcher(src);
        while( matcher.find() ){
            String g = matcher.group();
            if( table.getDtoName().equals(g) ){
                g = table.getDbTableName();
            } else {
                g = dtoToDbMap.get(g);
            }
            matcher.appendReplacement(sb, g);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
