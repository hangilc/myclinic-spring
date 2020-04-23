package jp.chang.myclinic.drawerprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.pdf.PdfPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static class CmdArgs {
        public String input;
        public String pdf;
        public String pdfPageSize;
        public double pdfShrink = 0;

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
                    case "--pdf-page-size":
                    case "--pdf-paper-size": {
                        this.pdfPageSize = args[++i];
                        break;
                    }
                    case "--pdf-shrink": {
                        String opt = args[++i];
                        try {
                            this.pdfShrink = Double.parseDouble(opt);
                        } catch(NumberFormatException ex){
                            System.err.println("Invalid --pdf-shrink option: " + opt);
                            System.err.println("This option should be a number (in mm unit)");
                            System.exit(1);
                        }
                        break;
                    }
                    default: {
                        System.err.println("Invalid argument: " + arg);
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
            System.err.println("    --pdf-page-size SIZE: A5, 148x210, ...");
            System.err.println("    --pdf-shrink AMOUNT: shrink output with AMOUNT margin (unit: mm)");
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
            InputStream fin = new FileInputStream(cmdArgs.input);
            reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
        }
        pages = mapper.readValue(reader, new TypeReference<List<List<Op>>>() {
        });
        reader.close();
        if (cmdArgs.pdf != null) {
            PaperSize paperSize = getPaperSize(cmdArgs.pdfPageSize);
            PdfPrinter printer = new PdfPrinter(paperSize);
            if( cmdArgs.pdfShrink != 0.0 ){
                printer.setShrink(cmdArgs.pdfShrink);
            }
            printer.print(pages, cmdArgs.pdf);
        } else {
            PrinterEnv penv = new PrinterEnv(Paths.get(getPrinterSettingsDir()));
            penv.print(pages, null);
        }
    }

    private static String getPrinterSettingsDir() {
        return System.getenv("MYCLINIC_PRINTER_SETTINGS_DIR");
    }

    private final static Pattern patPaperSize = Pattern.compile("([\\d.]+)[Xx]([\\d.]+)");

    private static PaperSize getPaperSize(String opt) {
        if (opt == null || opt.isEmpty()) {
            return PaperSize.A4;
        }
        Matcher m = patPaperSize.matcher(opt);
        if (m.matches()) {
            double w = Double.parseDouble(m.group(1));
            double h = Double.parseDouble(m.group(2));
            return new PaperSize(w, h);
        }
        switch (opt) {
            case "A4":
                return PaperSize.A4;
            case "A4-landscape":
                return PaperSize.A4_Landscape;
            case "A5":
                return PaperSize.A5;
            case "A5-landscape":
                return PaperSize.A5_Landscape;
            case "A6":
                return PaperSize.A6;
            case "A6-landscape":
                return PaperSize.A6_Landscape;
            case "B4":
                return PaperSize.B4;
            case "B4-landscape":
                return PaperSize.B4_Landscape;
            case "B5":
                return PaperSize.B5;
            case "B5-landscape":
                return PaperSize.B5_Landscape;
            case "B6":
                return PaperSize.B6;
            case "B6-landscape":
                return PaperSize.B6_Landscape;
            default:
                throw new RuntimeException("Unknown paper size: " + opt);
        }
    }
}

