package jp.chang.myclinic.support.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigTemp implements ConfigService {

    private final Map<String, String> cache = new ConcurrentHashMap<>();

    public ConfigTemp(Map<String, String> initialValues){
        batchUpdate(initialValues);
    }

    public ConfigTemp(){
        this(Collections.emptyMap());
    }

    @Override
    public String getValue(String key) {
        return cache.get(key);
    }

    @Override
    public void setValue(String key, String value) {
        cache.put(key, value);
    }

    @Override
    public void batchUpdate(Map<String, String> values) {
        for(Map.Entry<String, String> entry: values.entrySet()){
            cache.put(entry.getKey(), entry.getValue());
        }
    }

    public static void main(String[] args){
        ConfigService service = new ConfigPropertyFile(Paths.get("work/mock-config.property"));
        confirm(service.getValue("non") == null);
        service.setValue("addr", "東京都");
        confirm(service.getValue("addr").equals("東京都"));
        Map<String, String> values = new HashMap<>();
        values.put("a", "あ");
        values.put("b", "い");
        service.batchUpdate(values);
        confirm(service.getValue("addr").equals("東京都"));
        confirm(service.getValue("a").equals("あ"));
        confirm(service.getValue("b").equals("い"));
    }

    private static void confirm(boolean value){
        if( !value ){
            throw new RuntimeException("confirmation failure");
        }
    }

}
