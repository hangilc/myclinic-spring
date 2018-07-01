package jp.chang.myclinic.myclinicenv;

import jp.chang.myclinic.drawer.printer.PrinterEnv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MyclinicEnv {

    //private static Logger logger = LoggerFactory.getLogger(MyclinicEnv.class);

    private String appName;
    private Path baseDir;

    public MyclinicEnv(String appName) {
        this.appName = appName;
        this.baseDir = Paths.get(System.getProperty("user.home"), "myclinic-env");
    }

    public PrinterEnv getPrinterEnv() {
        Path printerSettingDir = getPrinterSettingDir();
        ensureDirectory(printerSettingDir);
        return new PrinterEnv(printerSettingDir);
    }

    private Path getPrinterSettingDir() {
        ensureDirectory(baseDir);
        return baseDir.resolve("printer-settings");
    }

    public Properties getAppProperties() {
        Path path = getAppPropertiesPath();
        ensureFile(path);
        return readProperties(path);
    }

    public String getAppProperty(String key){
        Properties props = getAppProperties();
        if( props != null ){
            return props.getProperty(key);
        } else {
            return null;
        }
    }

    private Path getAppPropertiesPath() {
        ensureDirectory(baseDir);
        return baseDir.resolve(String.format("%s.properties", appName));
    }

    public void saveAppProperties(Properties props){
        Path path = getAppPropertiesPath();
        saveProperties(path, props);
    }

    public void saveAppProperty(String key, String value){
        Properties props = getAppProperties();
        props.put(key, value);
        saveAppProperties(props);
    }

    public Path createTempDir(String prefix){
        try {
            return Files.createTempDirectory(baseDir, prefix);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Properties readProperties(Path path) {
        Properties props = new Properties();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return props;
    }

    private void saveProperties(Path path, Properties props) {
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            props.store(writer, "");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void ensureFile(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void ensureDirectory(Path dir) {
        try {
            if (!Files.isDirectory(dir)) {
                Files.createDirectory(dir);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
