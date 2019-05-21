package jp.chang.myclinic.practice;

import javax.ws.rs.RuntimeType;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PracticeConfig {

    private static class Item {
        String key;
        BiConsumer<PracticeConfig, String> fieldSetter;
        Function<PracticeConfig, String> fieldReader;

        Item(String key,
             BiConsumer<PracticeConfig, String> fieldSetter,
             Function<PracticeConfig, String> fieldReader) {
            this.key = key;
            this.fieldSetter = fieldSetter;
            this.fieldReader = fieldReader;
        }
    }

    private List<Item> items = List.of(
            new Item("shohousen-printer-setting",
                    (c, s) -> c.shohousenPrinterSetting = s,
                    c -> c.shohousenPrinterSetting),
            new Item("refer-printer-setting",
                    (c, s) -> c.referPrinterSetting = s,
                    c -> c.referPrinterSetting),
            new Item("kouhatsu-kasan",
                    (c, s) -> c.kouhatsuKasan = s,
                    c -> c.kouhatsuKasan)
    );

    private Map<String, String> unknownEntries = new HashMap<>();

    private String shohousenPrinterSetting;
    private String referPrinterSetting;
    private String kouhatsuKasan;

    public String getShohousenPrinterSetting() {
        return shohousenPrinterSetting;
    }

    public void setShohousenPrinterSetting(String shohousenPrinterSetting) {
        this.shohousenPrinterSetting = shohousenPrinterSetting;
    }

    public String getReferPrinterSetting() {
        return referPrinterSetting;
    }

    public void setReferPrinterSetting(String referPrinterSetting) {
        this.referPrinterSetting = referPrinterSetting;
    }

    public String getKouhatsuKasan() {
        return kouhatsuKasan;
    }

    public void setKouhatsuKasan(String kouhatsuKasan) {
        this.kouhatsuKasan = kouhatsuKasan;
    }

    public void saveToPropertyFile(Path path) {
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            try(Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                saveToWriter(writer);
            }
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    public String saveToPropertiesString(){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        try {
            saveToWriter(writer);
            return out.toString(StandardCharsets.UTF_8);
        } catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    private void saveToWriter(Writer writer) throws IOException {
        Properties props = new Properties();
        for (Item item : items) {
            props.setProperty(item.key, item.fieldReader.apply(this));
        }
        for (String key : unknownEntries.keySet()) {
            String value = unknownEntries.get(key);
            props.setProperty(key, value);
        }
        props.store(writer, "");
    }

    private void handleEntry(String key, String value) {
        for (Item item : items) {
            if (item.key.equals(key)) {
                item.fieldSetter.accept(this, value);
                return;
            }
        }
        unknownEntries.put(key, value);
    }

    public void loadFromPropertiesFile(Path path) {
        Properties props = readPropertyFile(path);
        loadFromProperties(props);
    }

    public void loadFromPropertiesString(String data) {
        InputStream in = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        Properties props = new Properties();
        try {
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
            loadFromProperties(props);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFromProperties(Properties props) {
        for (Object keyObj : props.keySet()) {
            if (keyObj instanceof String) {
                String key = (String) keyObj;
                String value = props.getProperty(key);
                handleEntry(key, value);
            }
        }
    }

    private Properties readPropertyFile(Path path) {
        Properties props = new Properties();
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                props.load(reader);
            }
            return props;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
