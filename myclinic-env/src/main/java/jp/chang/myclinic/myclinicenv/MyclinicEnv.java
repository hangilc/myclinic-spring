package jp.chang.myclinic.myclinicenv;

import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class MyclinicEnv {

    private static Logger logger = LoggerFactory.getLogger(MyclinicEnv.class);

    private Path baseDir;

    private static Path defaultBaseDir(){
        return Paths.get(System.getProperty("user.home"), "myclinic-env");
    }

    public MyclinicEnv() throws IOException {
        this(null);
    }

    public MyclinicEnv(Path baseDir) throws IOException {
        if( baseDir == null ){
            baseDir = defaultBaseDir();
        }
        this.baseDir = baseDir;
    }

    public PrinterEnv getPrinterEnv() throws IOException {
        Path printerSettingDir = getPrinterSettingDir();
        ensureDirectory(printerSettingDir);
        return new PrinterEnv(printerSettingDir);
    }

    public Properties getAppProperties(String app) throws IOException {
        Path path = getAppPropertiesPath(app);
        ensureFile(path);
        return readProperties(path);
    }

    public void saveAppProperties(String app, Properties props) throws IOException {
        Path path = getAppPropertiesPath(app);
        saveProperties(path, props);
    }

    private Properties readProperties(Path path) throws IOException {
        Properties props = new Properties();
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            props.load(reader);
        }
        return props;
    }

    private void saveProperties(Path path, Properties props) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
            props.store(writer, "");
        }
    }

    private Path getAppPropertiesPath(String app) throws IOException {
        ensureDirectory(baseDir);
        return baseDir.resolve(String.format("%s.properties", app));
    }

    private void ensureFile(Path path) throws IOException {
        if( !Files.exists(path) ){
            Files.createFile(path);
        }
    }

    private void ensureDirectory(Path dir) throws IOException {
        if( !Files.isDirectory(dir) ){
            Files.createDirectory(dir);
        }
    }

    private Path getPrinterSettingDir() throws IOException {
        ensureDirectory(baseDir);
        return baseDir.resolve("printer-settings");
    }

}
