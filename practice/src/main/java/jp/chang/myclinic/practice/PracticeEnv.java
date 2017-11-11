package jp.chang.myclinic.practice;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PracticeEnv {

    private static final Logger logger = LoggerFactory.getLogger(PracticeEnv.class);

    public static PracticeEnv INSTANCE;

    private Path printerSettingsDir;
    private ClinicInfoDTO clinicInfo;

    public PracticeEnv(CommandArgs commandArgs){
        printerSettingsDir = commandArgs.getWorkingDirectory();
        if( printerSettingsDir == null ){
            printerSettingsDir = Paths.get(System.getProperty("user.dir"));
        }
        if( !(Files.exists(printerSettingsDir) && Files.isDirectory(printerSettingsDir)) ){
            logger.error("Invalid printer settings directory: " + printerSettingsDir);
            System.exit(1);
        }
    }

    public Path getPrinterSettingsDir() {
        return printerSettingsDir;
    }

    public void setPrinterSettingsDir(Path printerSettingsDir) {
        this.printerSettingsDir = printerSettingsDir;
    }

    public ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public void setClinicInfo(ClinicInfoDTO clinicInfo) {
        this.clinicInfo = clinicInfo;
    }

    @Override
    public String toString() {
        return "PracticeEnv{" +
                "printerSettingsDir=" + printerSettingsDir +
                ", clinicInfo=" + clinicInfo +
                '}';
    }
}
