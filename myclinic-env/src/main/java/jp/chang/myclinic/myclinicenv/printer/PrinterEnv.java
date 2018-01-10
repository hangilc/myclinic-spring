package jp.chang.myclinic.myclinicenv.printer;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrinterEnv {

    private Path baseDir;

    public PrinterEnv(Path baseDir){
        this.baseDir = baseDir;
    }

    private Path devnamesSettingPath(String name){
        return baseDir.resolve(name + ".devnames");
    }

    private Path devmodeSettingPath(String name){
        return baseDir.resolve(name + ".devmode");
    }

    private Path auxSettingPath(String name){
        return baseDir.resolve(name + ".json");
    }

    public List<String> listSettingNames() throws IOException {
        if( baseDir == null ){
            return Collections.emptyList();
        }
        List<String> names = new ArrayList<>();
        for (Path path : Files.newDirectoryStream(baseDir, "*.devnames")) {
            String fileName = path.getFileName().toString();
            int pos = fileName.lastIndexOf('.');
            String name = fileName.substring(0, pos);
            names.add(name);
        }
        return names;
    }


//    public PrintResult print(List<List<Op>> pages, String settingName){
//        DrawerPrinter drawerPrinter = new DrawerPrinter();
//        byte[] devmode = null, devnames = null;
//        AuxSetting auxSetting = null;
//        try {
//            if( settingName == null || settingName.isEmpty() ) {
//                DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
//                if (result.ok) {
//                    devmode = result.devmodeData;
//                    devnames = result.devnamesData;
//                    auxSetting = null;
//                } else {
//                    return PrintResult.OK;
//                }
//            } else if( settingDir == null ){
//                return PrintResult.SettingDirNotSpecified;
//            } else {
//                if( !nameExists(settingName) ){
//                    return PrintResult.NoSuchSetting;
//                }
//                devmode = readDevmode(settingName);
//                devnames = readDevnames(settingName);
//                auxSetting = readAuxSetting(settingName);
//            }
//            drawerPrinter.printPages(pages, devmode, devnames, auxSetting);
//            return PrintResult.OK;
//        } catch(IOException ex){
//            ex.printStackTrace();
//            return PrintResult.IOError;
//        }
//    }

//    public boolean createNewSetting(String name) throws IOException, SettingDirNotSuppliedException {
//        if (settingDir == null) {
//            throw new SettingDirNotSuppliedException();
//        }
//        DrawerPrinter drawerPrinter = new DrawerPrinter();
//        DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
//        if( !result.ok ){
//            return false;
//        }
//        byte[] devmode = result.devmodeData;
//        byte[] devnames = result.devnamesData;
//        AuxSetting auxSetting = new AuxSetting();
//        saveSetting(name, devnames, devmode, auxSetting);
//        return true;
//    }

//    public void deleteSetting(String name) throws IOException {
//        Files.delete(devnamesSettingPath(name));
//        Files.delete(devmodeSettingPath(name));
//        Files.delete(auxSettingPath(name));
//    }
//
//    public void saveSetting(String name, byte[] devnames, byte[] devmode)
//            throws SettingDirNotSuppliedException, IOException {
//        if (settingDir == null) {
//            throw new SettingDirNotSuppliedException();
//        }
//        Files.write(devnamesSettingPath(name), devnames);
//        Files.write(devmodeSettingPath(name), devmode);
//    }
//
//    public void saveSetting(String name, AuxSetting auxSetting) throws SettingDirNotSuppliedException, IOException {
//        if (settingDir == null) {
//            throw new SettingDirNotSuppliedException();
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.writeValue(new File(auxSettingPath(name).toString()), auxSetting);
//    }
//
//
//    public void saveSetting(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting)
//            throws SettingDirNotSuppliedException, IOException {
//        saveSetting(name, devnames, devmode);
//        saveSetting(name, auxSetting);
//    }
//
//    private boolean nameExists(String name){
//        return Files.exists(devnamesSettingPath(name)) &&
//                Files.exists(devmodeSettingPath(name));
//    }

//    public byte[] readDevnames(String name) throws IOException {
//        return Files.readAllBytes(devnamesSettingPath(name));
//    }
//
//    public byte[] readDevmode(String name) throws IOException {
//        return Files.readAllBytes(devmodeSettingPath(name));
//    }
//
//    public AuxSetting readAuxSetting(String name) throws IOException {
//        Path path = auxSettingPath(name);
//        if( Files.exists(path) ) {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(new File(auxSettingPath(name).toString()), AuxSetting.class);
//        } else {
//            return new AuxSetting();
//        }
//    }

}
