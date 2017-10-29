package jp.chang.myclinic.reception;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ReceptionConfig {

    private static final String keyPrinterSettingName = "printer-setting-name";

    private String printerSettingName;

    public ReceptionConfig(){

    }

    public String getPrinterSettingName() {
        return printerSettingName;
    }

    public void setPrinterSettingName(String printerSettingName) {
        this.printerSettingName = printerSettingName;
    }

    public void loadFromFile(Path configFilePath) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(configFilePath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.load(reader);
            String settingName = props.getProperty(keyPrinterSettingName);
            if( settingName != null ){
                this.printerSettingName = settingName;
            }
        }
    }

    public void saveToFile(Path configFilePath) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(configFilePath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            if( printerSettingName != null ) {
                props.setProperty(keyPrinterSettingName, printerSettingName);
            }
            props.store(writer, "");
        }
    }

    @Override
    public String toString() {
        return "ReceptionConfig{" +
                "printerSettingName='" + printerSettingName + '\'' +
                '}';
    }

}
