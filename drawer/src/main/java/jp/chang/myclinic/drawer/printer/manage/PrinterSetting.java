package jp.chang.myclinic.drawer.printer.manage;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangil on 2017/05/24.
 */
public class PrinterSetting {

    private Path settingDir;

    public static PrinterSetting INSTANCE;

    static {
        Path path = Paths.get(System.getProperty("user.home"), "printer-settings");
        System.out.println("printer setting path: " + path);
        INSTANCE = new PrinterSetting(path);
        try {
            INSTANCE.ensureSettingDir();
        } catch(IOException ex){
            throw new UncheckedIOException(ex);
        }
    }

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
        Path path = auxSettingPath(name);
        if( Files.exists(path) ) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(auxSettingPath(name).toString()), AuxSetting.class);
        } else {
            return new AuxSetting();
        }
    }

    public boolean nameExists(String name){
        return Files.exists(devnamesSettingPath(name)) &&
                Files.exists(devmodeSettingPath(name));
    }

    public List<String> listNames() throws IOException {
        List<String> names = new ArrayList<>();
        for(Path path: Files.newDirectoryStream(settingDir, "*.devnames")){
            String fileName = path.getFileName().toString();
            int pos = fileName.lastIndexOf('.');
            String name = fileName.substring(0, pos);
            names.add(name);
        }
        return names;
    }

    public void deleteSetting(String name) throws IOException {
        Files.delete(devnamesSettingPath(name));
        Files.delete(devmodeSettingPath(name));
        Files.delete(auxSettingPath(name));
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

    public void printPages(List<List<Op>> pages, String settingName){
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        byte[] devmode = null, devnames = null;
        AuxSetting auxSetting = null;
        try {
            if( settingName == null || settingName.isEmpty() ){
                DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
                if (result.ok) {
                    devmode = result.devmodeData;
                    devnames = result.devnamesData;
                    auxSetting = null;
                } else {
                    return;
                }

            } else {
                if( !INSTANCE.nameExists(settingName) ){
                    throw new RuntimeException(settingName + ": この名前の印刷設定を見つけられません。");
                }
                devmode = readDevmode(settingName);
                devnames = readDevnames(settingName);
                auxSetting = readAuxSetting(settingName);
            }
            drawerPrinter.printPages(pages, devmode, devnames, auxSetting);
        } catch(IOException ex){
            throw new UncheckedIOException(ex);
        }
    }

    public void print(List<Op> ops, String settingName){
        List<List<Op>> pages = new ArrayList<>();
        pages.add(ops);
        printPages(pages, settingName);
    }

}
