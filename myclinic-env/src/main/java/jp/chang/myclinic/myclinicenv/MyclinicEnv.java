package jp.chang.myclinic.myclinicenv;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static Logger logger = LoggerFactory.getLogger(MyclinicEnv.class);

    private Path baseDir;

    private static Path defaultBaseDir() {
        return Paths.get(System.getProperty("user.home"), "myclinic-env");
    }

    public MyclinicEnv() {
        this(null);
    }

    public MyclinicEnv(Path baseDir) {
        if (baseDir == null) {
            baseDir = defaultBaseDir();
        }
        this.baseDir = baseDir;
    }

    public PrinterEnv getPrinterEnv() {
        Path printerSettingDir = getPrinterSettingDir();
        ensureDirectory(printerSettingDir);
        return new PrinterEnv(printerSettingDir);
    }

    public Properties getAppProperties(String app) {
        Path path = getAppPropertiesPath(app);
        ensureFile(path);
        return readProperties(path);
    }

    public void saveAppProperties(String app, Properties props) throws IOException {
        Path path = getAppPropertiesPath(app);
        saveProperties(path, props);
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

    private Path getAppPropertiesPath(String app) {
        ensureDirectory(baseDir);
        return baseDir.resolve(String.format("%s.properties", app));
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

    private Path getPrinterSettingDir() {
        ensureDirectory(baseDir);
        return baseDir.resolve("printer-settings");
    }

}
