package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;

public class MockPrinterEnv {

    public static PrinterEnv create(){
        String tempPrinterSettingDir = "./work/myclinic-env/printer-settings";
        ensurePrinterSettingDir(tempPrinterSettingDir);
        return new PrinterEnv(Paths.get(tempPrinterSettingDir));
    }

    private static void ensurePrinterSettingDir(String path){
        File file = new File(path);
        if( !file.exists() ) {
            boolean ok = file.mkdirs();
            if (!ok) {
                throw new RuntimeException("failed to create direcotry: " + path);
            }
        }
    }

}
