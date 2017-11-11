package jp.chang.myclinic.reception;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReceptionEnv {
    public static ReceptionEnv INSTANCE = new ReceptionEnv();

    private static final Logger logger = LoggerFactory.getLogger(ReceptionEnv.class);

    private Path workdir;
    private Path configFilePath;
    private ReceptionConfig config;
    private Path printerSettingsDir;
    private ClinicInfoDTO clinicInfo;

    public void updateWithArgs(ReceptionArgs args){
        this.workdir = args.workingDirPath;
        if( this.workdir == null ){
            this.workdir = Paths.get(System.getProperty("user.dir"));
        }
        this.configFilePath = args.configFilePath;
        this.config = new ReceptionConfig();
        if( this.configFilePath != null && Files.exists(this.configFilePath) ){
            try {
                loadConfig();
            } catch(IOException ex){
                logger.error("設定ファイルの読み込みに失敗しました。", ex);
                System.exit(1);
            }
        }
        this.printerSettingsDir = args.printerSettingsDir;
        if( this.printerSettingsDir == null ){
            this.printerSettingsDir = workdir;
        }
    }

    public Path getWorkdir(){
        return workdir;
    }

    public Path getConfigFilePath() {
        return configFilePath;
    }

    public void setConfigFilePath(Path configFilePath) {
        this.configFilePath = configFilePath;
    }

    public ReceptionConfig getConfig(){
        return config;
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

    public ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo) {
        this.clinicInfo = clinicInfo;
    }

    @Override
    public String toString() {
        return "ReceptionEnv{" +
                "workdir=" + workdir +
                ", configFilePath=" + configFilePath +
                ", config=" + config +
                ", printerSettingsDir=" + printerSettingsDir +
                ", clinicInfo=" + clinicInfo +
                '}';
    }
}
