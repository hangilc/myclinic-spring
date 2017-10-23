package jp.chang.myclinic.drawer.printer.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.lib.Result;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PrintManager {
    private Path settingDir;

    public PrintManager(Path settingDir){
        this.settingDir = settingDir;
    }

    public PrintResult print(List<List<Op>> pages, String settingName){
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        byte[] devmode = null, devnames = null;
        AuxSetting auxSetting = null;
        try {
            if( settingName == null || settingName.isEmpty() ) {
                DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
                if (result.ok) {
                    devmode = result.devmodeData;
                    devnames = result.devnamesData;
                    auxSetting = null;
                } else {
                    return PrintResult.OK;
                }
            } else if( settingDir == null ){
                return PrintResult.SettingDirNotSpecified;
            } else {
                if( !nameExists(settingName) ){
                    return PrintResult.NoSuchSetting;
                }
                devmode = readDevmode(settingName);
                devnames = readDevnames(settingName);
                auxSetting = readAuxSetting(settingName);
            }
            drawerPrinter.printPages(pages, devmode, devnames, auxSetting);
            return PrintResult.OK;
        } catch(IOException ex){
            ex.printStackTrace();
            return PrintResult.IOError;
        }
    }

    public SaveSettingResult saveSetting(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting) {
        if( settingDir == null ){
            return SaveSettingResult.SettingDirNotSpecified;
        }
        try {
            Files.write(devnamesSettingPath(name), devnames);
            Files.write(devmodeSettingPath(name), devmode);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(auxSettingPath(name).toString()), auxSetting);
            return SaveSettingResult.OK;
        } catch(IOException ex){
            ex.printStackTrace();
            return SaveSettingResult.IOException;
        }
    }

    public Result<List<String>, ListSettingNamesError> listNames() {
        if( settingDir == null ){
            return new Result<>(null, ListSettingNamesError.SettingDirNotSpecified);
        }
        List<String> names = new ArrayList<>();
        try {
            for (Path path : Files.newDirectoryStream(settingDir, "*.devnames")) {
                String fileName = path.getFileName().toString();
                int pos = fileName.lastIndexOf('.');
                String name = fileName.substring(0, pos);
                names.add(name);
            }
            return new Result<>(names);
        } catch(IOException ex){
            ex.printStackTrace();
            return new Result<>(null, ListSettingNamesError.IOException);
        }
    }

    private boolean nameExists(String name){
        return Files.exists(devnamesSettingPath(name)) &&
                Files.exists(devmodeSettingPath(name));
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

    private byte[] readDevnames(String name) throws IOException {
        return Files.readAllBytes(devnamesSettingPath(name));
    }

    private byte[] readDevmode(String name) throws IOException {
        return Files.readAllBytes(devmodeSettingPath(name));
    }

    private AuxSetting readAuxSetting(String name) throws IOException {
        Path path = auxSettingPath(name);
        if( Files.exists(path) ) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(auxSettingPath(name).toString()), AuxSetting.class);
        } else {
            return new AuxSetting();
        }
    }


}
