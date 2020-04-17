package jp.chang.myclinic.drawerprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.pdf.PdfPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {

    private static class CmdArgs {
        public String input;
        public String pdf;

        public CmdArgs(String[] args) {
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-i":
                    case "--in": {
                        this.input = args[i + 1];
                        i += 1;
                        break;
                    }
                    case "--pdf": {
                        this.pdf = args[++i];
                        break;
                    }
                    default: {
                        usage();
                        System.exit(1);
                    }
                }
            }
        }

        public static void usage() {
            System.err.println("Usage: drawer-printer [options] [INPUT]");
            System.err.println("  options:");
            System.err.println("    -i, --in INPUT: read from file");
            System.err.println("    --pdf OUTPUT: write as PDF file to OUTPUT");
        }
    }

    public static void main(String[] args) throws Exception {
        CmdArgs cmdArgs = new CmdArgs(args);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Op.class, new JacksonOpDeserializer());
        mapper.registerModule(module);
        List<List<Op>> pages = null;
        InputStreamReader reader = null;
        if (cmdArgs.input == null) {
            reader = new InputStreamReader(System.in, Charset.defaultCharset());
        } else {
            System.out.println(cmdArgs.input);
            InputStream fin = new FileInputStream(cmdArgs.input);
            reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
        }
        pages = mapper.readValue(reader, new TypeReference<List<List<Op>>>(){});
        reader.close();
        if( cmdArgs.pdf != null ){
            PdfPrinter printer = new PdfPrinter();
            printer.print(pages, cmdArgs.pdf);
        } else {
            PrinterEnv penv = new PrinterEnv(Paths.get(getPrinterSettingsDir()));
            penv.print(pages, null);
        }
    }

    private static String getPrinterSettingsDir() {
        return System.getenv("MYCLINIC_PRINTER_SETTINGS_DIR");
    }
}

