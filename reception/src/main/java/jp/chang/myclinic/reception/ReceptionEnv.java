package jp.chang.myclinic.reception;

import java.io.IOException;
import java.nio.file.Path;

public class ReceptionEnv {

    private Path configFilePath;
    private ReceptionConfig config;
    private Path printerSettingsDir;

    public ReceptionEnv(){
        config = new ReceptionConfig();
    }

    public Path getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(Path configFilePath) {
        this.configFilePath = configFilePath;
    }

    public void loadConfig() throws IOException {
        config.loadFromFile(configFilePath);
    }

    public void saveConfig() throws IOException {
        config.saveToFile(configFilePath);
    }

    public Path getPrinterSettingsDir() {
        return printerSettingsDir;
    }

    public void setPrinterSettingsDir(Path printerSettingsDir) {
        this.printerSettingsDir = printerSettingsDir;
    }

    public String getPrinterSettingName(){
        return config.getPrinterSettingName();
    }
}
