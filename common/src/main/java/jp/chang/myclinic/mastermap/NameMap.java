package jp.chang.myclinic.mastermap;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hangil on 2017/03/04.
 */
class NameMap {
    private HashMap<String, Integer> map;

    NameMap(){
        this.map = new HashMap<>();
    }

    Optional<Integer> get(String name){
        Integer bind = map.get(name);
        return Optional.ofNullable(bind);
    }

    private Pattern pattern = Pattern.compile("^(?:s|k|d|a),([^,]+),(\\d+)");

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
