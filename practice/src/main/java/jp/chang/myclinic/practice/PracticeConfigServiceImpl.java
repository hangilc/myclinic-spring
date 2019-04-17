package jp.chang.myclinic.practice;

import javafx.application.Platform;
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

public class PracticeConfigServiceImpl implements PracticeConfigService {

    private static Logger logger = LoggerFactory.getLogger(PracticeConfigServiceImpl.class);

    private Path propertyPath;
    private Properties cache;

    public PracticeConfigServiceImpl(String propertyFile) {
        this.propertyPath = Paths.get(propertyFile);
        if( !Files.exists(propertyPath) ){
            try {
                Files.createFile(propertyPath);
            } catch (IOException e) {
                System.err.println("Cannot create config property file: " + propertyFile);
                Platform.exit();
            }
        }
        this.cache = readProperties(Paths.get(propertyFile));
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

    @Override
    public String getShohousenPrinterSetting() {
        return cache.getProperty("shohousen-printer-setting");
    }

    @Override
    public void setShohousenPrinterSetting(String settingName) {
        cache.setProperty("shohousen-printer-setting", settingName);
        saveProperties(propertyPath, cache);
    }

    @Override
    public String getKouhatsuKasan() {
        return cache.getProperty("kouhatsu-kasan");
    }

    @Override
    public PrinterEnv getPrinterEnv() {
        return new PrinterEnv();
    }
}
