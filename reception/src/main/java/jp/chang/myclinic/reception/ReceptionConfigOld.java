package jp.chang.myclinic.reception;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ReceptionConfigOld {

    public static ReceptionConfigOld INSTANCE = new ReceptionConfigOld();

    private Path settingDir = Paths.get(System.getProperty("user.home"), "printer-settings");
    private Path configPath = Paths.get(System.getProperty("user.home"), "myclinic-reception.properties");

    private String currentSetting = "";

    private ReceptionConfigOld(){

    }

    public Path getSettingDir(){
        return settingDir;
    }

    public String getCurrentSetting() {
        return currentSetting;
    }

    public void setCurrentSetting(String currentSetting) {
        this.currentSetting = currentSetting;
    }

    public void ensureConfigFile() throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.setProperty("printer-setting-name", "");
            props.store(writer, "");
        }
    }

    public void readFromConfigFile() throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.load(reader);
            String settingName = props.getProperty("printer-setting-name");
            if( settingName != null ){
                ReceptionConfigOld.INSTANCE.setCurrentSetting(settingName);
            }
        }
    }

    public void writeToConfigFile() throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(configPath, StandardCharsets.UTF_8)){
            Properties props = new Properties();
            props.setProperty("printer-setting-name", currentSetting);
            props.store(writer, "");
        }
    }
}
