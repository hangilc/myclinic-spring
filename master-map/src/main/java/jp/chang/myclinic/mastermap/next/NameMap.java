package jp.chang.myclinic.mastermap.next;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameMap {

    private Map<String, Integer> map = new HashMap<>();
    private Pattern pattern = Pattern.compile("^(?:s|k|d|a),([^,]+),(\\d+)");

    NameMap() {

    }

    public Integer get(String name){
        return map.get(name);
    }

    void put(String name, int code){
        map.put(name, code);
    }

    void parseAndEnter(String src){
        Matcher matcher = pattern.matcher(src);
        if( !matcher.matches() ){
            throw new RuntimeException("invalid name map entry: " + src);
        }
        String name = matcher.group(1);
        String code = matcher.group(2);
        int codeValue = Integer.parseInt(code);
        map.put(name, codeValue);
    }

}
