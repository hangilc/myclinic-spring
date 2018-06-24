package jp.chang.myclinic.myclinicenv.printer;


import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class PrinterEnv {

    private static Logger logger = LoggerFactory.getLogger(PrinterEnv.class);
    private Path baseDir;

    public PrinterEnv(Path baseDir) {
        this.baseDir = baseDir;
    }

    public void print(List<Op> ops, String settingName) throws IOException {
        printPages(Collections.singletonList(ops), settingName);
    }

    public void printPages(List<List<Op>> pages, String settingName) throws IOException {
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        byte[] devmode = null, devnames = null;
        AuxSetting auxSetting = null;
        if (settingName == null || settingName.isEmpty()) {
            DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
            if (result.ok) {
                devmode = result.devmodeData;
                devnames = result.devnamesData;
                auxSetting = null;
            } else {
                return;
            }
        } else {
            devmode = getDevmode(settingName);
            devnames = getDevnames(settingName);
            auxSetting = getAuxSetting(settingName);
        }
        drawerPrinter.printPages(pages, devmode, devnames, auxSetting);
    }

    public List<String> listSettingNames() throws IOException {
        if (baseDir == null) {
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

    public boolean settingExists(String name){
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        return manager.settingExists(name);
    }

    private Path getSettingMapPath() {
        if (baseDir == null) {
            return null;
        } else {
            return baseDir.resolve("setting-map.properties");
        }
    }

    private void ensureSettingMap() throws IOException {
        Path path = getSettingMapPath();
        if (path != null) {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        }
    }

    private Properties readSettingMap() throws IOException {
        ensureSettingMap();
        Path path = getSettingMapPath();
        if (path == null) {
            return null;
        } else {
            try (InputStream inputStream = Files.newInputStream(path)) {
                Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                Properties props = new Properties();
                props.load(reader);
                return props;
            }
        }
    }

    private void saveSettingMap(Properties props) throws IOException {
        ensureSettingMap();
        Path path = getSettingMapPath();
        if (path == null) {
            logger.info("Setting has not been saved becasue baseDir is null.");
        } else {
            try (OutputStream outputStream = Files.newOutputStream(path,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                props.store(writer, "Program printer setting map");
            }
        }
    }

    public void savePrinterAuxSetting(String name, AuxSetting auxSetting) throws IOException, jp.chang.myclinic.drawer.printer.PrinterEnv.SettingDirNotSuppliedException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        manager.saveSetting(name, auxSetting);
    }

    public void savePrintSetting(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting)
            throws IOException, jp.chang.myclinic.drawer.printer.PrinterEnv.SettingDirNotSuppliedException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        try {
            manager.saveSetting(name, devnames, devmode);
        } catch (Exception ex) {
            logger.error("Failed to create printer setting.", ex);
            throw ex;
        }
        try {
            manager.saveSetting(name, auxSetting);
        } catch (Exception ex) {
            logger.error("Failed to printersetting auxSetting.", ex);
            manager.deleteSetting(name);
            throw ex;
        }
    }

    public void deletePrintSetting(String name) throws IOException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        manager.deleteSetting(name);
    }

    public void saveDefaultSettingName(String settingKey, String settingName) throws IOException {
        if (settingKey == null || settingKey.isEmpty()) {
            logger.info("saveSettingName did nothing because settingKey is empty");
            return;
        }
        Properties props = readSettingMap();
        if (props != null) {
            props.put(settingKey, settingName);
            saveSettingMap(props);
        } else {
            logger.info("Saving has not been done because baseDir is null.");
        }
    }

    public String getDefaultSettingName(String settingKey) throws IOException {
        if (settingKey == null || settingKey.isEmpty()) {
            return null;
        } else {
            Properties props = readSettingMap();
            if (props != null) {
                return props.getProperty(settingKey);
            } else {
                return null;
            }
        }
    }

    public byte[] getDevnames(String settingName) throws IOException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        return manager.readDevnames(settingName);
    }

    public byte[] getDevmode(String settingName) throws IOException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        return manager.readDevmode(settingName);
    }

    public AuxSetting getAuxSetting(String settingName) throws IOException {
        jp.chang.myclinic.drawer.printer.PrinterEnv manager = new jp.chang.myclinic.drawer.printer.PrinterEnv(baseDir);
        return manager.readAuxSetting(settingName);
    }

}
