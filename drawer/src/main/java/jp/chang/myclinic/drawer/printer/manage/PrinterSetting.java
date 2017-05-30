package jp.chang.myclinic.drawer.printer.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.printer.AuxSetting;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

    public List<String> listNames(){
        try {
            List<String> names = new ArrayList<>();
            for(Path path: Files.newDirectoryStream(settingDir, "*.devnames")){
                String fileName = path.getFileName().toString();
                int pos = fileName.lastIndexOf('.');
                String name = fileName.substring(0, pos);
                names.add(name);
            }
            return names;
        } catch(IOException ex){
            ex.printStackTrace();
            throw new UncheckedIOException(ex);
        }
    }

    public void ensureSettingDir() throws IOException {
        if( Files.exists(settingDir) ){
            if( Files.isDirectory(settingDir) ){
                return;
            }
            throw new RuntimeException("invalid setting dir: " + settingDir);
        } else {
            Files.createDirectory(settingDir);
        }
    }

}
