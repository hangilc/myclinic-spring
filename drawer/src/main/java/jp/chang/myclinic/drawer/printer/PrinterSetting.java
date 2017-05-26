package jp.chang.myclinic.drawer.printer;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by hangil on 2017/05/24.
 */
public class PrinterSetting {

    private Path settingDir;

    public PrinterSetting(Path settingDir){
        this.settingDir = settingDir;
    }

    private Path devnamesSettingPath(String name){
        return settingDir.resolve(name + ".devnames");
    }

    private Path devmodeSettingPath(String name){
        return settingDir.resolve(name + ".devmode");
    }

    private Path auxSettingPath(String name){
        return settingDir.resolve(name + ".json");
    }

    public void saveSetting(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting) throws IOException {
        Files.write(devnamesSettingPath(name), devnames);
        Files.write(devmodeSettingPath(name), devmode);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(auxSettingPath(name).toString()), auxSetting);
    }

    public byte[] readDevnames(String name) throws IOException {
        return Files.readAllBytes(devnamesSettingPath(name));
    }

    public byte[] readDevmode(String name) throws IOException {
        return Files.readAllBytes(devmodeSettingPath(name));
    }

    public AuxSetting readAuxSetting(String name) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(auxSettingPath(name).toString()), AuxSetting.class);
    }


}
