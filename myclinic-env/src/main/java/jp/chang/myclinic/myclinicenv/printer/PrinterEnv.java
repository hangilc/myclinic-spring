package jp.chang.myclinic.myclinicenv.printer;


import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.manager.PrintManager;
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

    public PrinterEnv(Path baseDir){
        this.baseDir = baseDir;
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

    private Path getSettingMapPath(){
        if( baseDir == null ){
            return null;
        } else {
            return baseDir.resolve("setting-map.properties");
        }
    }

    private void ensureSettingMap() throws IOException {
        Path path = getSettingMapPath();
        if( path != null ){
            if( !Files.exists(path) ){
                Files.createFile(path);
            }
        }
    }

    private Properties readSettingMap() throws IOException {
        ensureSettingMap();
        Path path = getSettingMapPath();
        if( path == null ){
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
        if( path == null ){
            logger.info("Setting has not been saved becasue baseDir is null.");
        } else {
            try (OutputStream outputStream = Files.newOutputStream(path,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                Writer writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                props.store(writer, "Program printer setting map");
            }
        }
    }

    public void createPrintSetting(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting)
            throws IOException, PrintManager.SettingDirNotSuppliedException {
        PrintManager manager = new PrintManager(baseDir);
        try {
            manager.saveSetting(name, devnames, devmode);
        } catch(Exception ex){
            logger.error("Failed to carete printer setting.", ex);
            throw ex;
        }
        try {
            manager.saveSetting(name, auxSetting);
        } catch(Exception ex){
            logger.error("Failed to create auxSetting.", ex);
            manager.deleteSetting(name);
            throw ex;
        }
    }

    public void saveDefaultSettingName(String settingKey, String settingName) throws IOException {
        if( settingKey == null || settingKey.isEmpty() ){
            logger.info("saveSettingName did nothing because settingKey is empty");
            return;
        }
        Properties props = readSettingMap();
        if( props != null ){
            props.put(settingKey, settingName);
            saveSettingMap(props);
        } else {
            logger.info("Saving has not been done because baseDir is null.");
        }
    }

    public String getDefaultSettingName(String settingKey) throws IOException {
        if( settingKey == null || settingKey.isEmpty()  ){
            return null;
        } else {
            Properties props = readSettingMap();
            return props.getProperty(settingKey);
        }
    }

}
