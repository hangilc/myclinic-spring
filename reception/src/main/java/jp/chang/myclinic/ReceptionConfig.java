package jp.chang.myclinic;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by hangil on 2017/05/24.
 */
public class ReceptionConfig {

    public static ReceptionConfig INSTANCE = new ReceptionConfig();

    private Path settingDir = Paths.get(".", "printer-settings");

    private ReceptionConfig(){

    }

    public Path getSettingDir(){
        return settingDir;
    }

    public void setSettingDir(Path path){
        settingDir = path;
    }
}
