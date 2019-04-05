package jp.chang.myclinic.support.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigPropertyFile implements ConfigService {

    private final Path propertyFile;
    private final Properties cache = new Properties();

    public ConfigPropertyFile(Path propertyFile) {
        this.propertyFile = propertyFile;
        load();
    }

    private synchronized void load(){
        try {
            if( Files.notExists(propertyFile) ){
                Files.createFile(propertyFile);
            }
            try (BufferedReader reader = Files.newBufferedReader(propertyFile, StandardCharsets.UTF_8)) {
                cache.load(reader);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private synchronized void save(){
        try {
            if( Files.notExists(propertyFile) ){
                Files.createFile(propertyFile);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(propertyFile, StandardCharsets.UTF_8)) {
                cache.store(writer, "");
            }
        } catch(IOException e){
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public synchronized String getValue(String key) {
        return cache.getProperty(key);
    }

    @Override
    public synchronized void setValue(String key, String value) {
        cache.setProperty(key, value);
        save();
    }

    @Override
    public synchronized void batchUpdate(Map<String, String> values) {
        for(Map.Entry<String, String> entry: values.entrySet()){
            cache.setProperty(entry.getKey(), entry.getValue());
        }
        save();
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
