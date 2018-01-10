package jp.chang.myclinic.myclinicenv;

import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
