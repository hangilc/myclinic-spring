package jp.chang.myclinic.shohousen.drawer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.shohousen.ShohousenData;
import jp.chang.myclinic.shohousen.ShohousenDrawer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        File inputFile = null;
        String printerSetting = null;
        if( args.length == 1 || args.length == 2 ){
            inputFile = new File(args[0]);
            if( args.length >= 2 ){
                printerSetting = args[1];
            }
        } else {
            usage();
            System.exit(1);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<ShohousenInput> inputs = mapper.readValue(inputFile, new TypeReference<List<ShohousenInput>>(){});
        for(ShohousenInput input: inputs){
            ShohousenData data = toData(input);
            ShohousenDrawer drawer = new ShohousenDrawer();
            data.applyTo(drawer);
            List<Op> ops = drawer.getOps();
            PrinterEnv penv = new PrinterEnv();
            penv.print(List.of(ops), printerSetting);
        }
    }

    private static void usage(){
        System.err.println("Usage: shohousen-drawer INPUT.JSON [PRINTER-SETTING]");
    }

    private static ShohousenData toData(ShohousenInput input){
        ShohousenData data = new ShohousenData();
        data.clinicAddress = input.clinicAddress;
        data.setDrugs(input.content);
        return data;
    }

    private static String getPrinterSettingsDir(){
        return System.getenv("MYCLINIC_PRINTER_SETTINGS_DIR");
    }
}
