package jp.chang.myclinic.shohousen.drawer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpSerializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.shohousen.ShohousenData;
import jp.chang.myclinic.shohousen.ShohousenDrawer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static class CmdArgs {
        public String input;
        public String output;
        public boolean help;

        public static CmdArgs parse(String[] args) {
            CmdArgs result = new CmdArgs();
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                switch (arg) {
                    case "-i":
                    case "--in": {
                        result.input = args[i + 1];
                        i += 1;
                        break;
                    }
                    case "-o":
                    case "--out": {
                        result.output = args[i + 1];
                        i += 1;
                        break;
                    }
                    case "-h": {
                        result.help = true;
                        break;
                    }
                    default: {
                        usage();
                        System.exit(1);
                    }
                }
            }
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        CmdArgs cmdArgs = CmdArgs.parse(args);
        InputStreamReader in;
        if (cmdArgs.input == null) {
            in = new InputStreamReader(System.in, Charset.defaultCharset());
        } else {
            FileInputStream fs = new FileInputStream(cmdArgs.input);
            in = new InputStreamReader(fs, StandardCharsets.UTF_8);
        }
        ObjectMapper mapper = new ObjectMapper();
        List<ShohousenInput> inputs = mapper.readValue(in, new TypeReference<List<ShohousenInput>>() {
        });
        List<List<Op>> result = new ArrayList<>();
        for (ShohousenInput input : inputs) {
            ShohousenData data = toData(input);
            ShohousenDrawer drawer = new ShohousenDrawer();
            data.applyTo(drawer);
            List<Op> ops = drawer.getOps();
            result.add(ops);
        }
        SimpleModule module = new SimpleModule();
        module.addSerializer(Op.class, new JacksonOpSerializer());
        mapper.registerModule(module);
        String json = mapper.writeValueAsString(result);
        if (cmdArgs.output == null) {
            System.out.println(json);
        } else {
            FileOutputStream fout = new FileOutputStream(cmdArgs.output);
            OutputStreamWriter out = new OutputStreamWriter(fout, StandardCharsets.UTF_8);
            out.write(json);
            out.write("\n");
            out.close();
        }
    }

    private static void usage() {
        System.err.println("Usage: shohousen-drawer [options");
        System.err.println("  options:");
        System.err.println("    -i, --in INPUT : read from file");
        System.err.println("    -o, --out OUTPUT : write to file");
        System.err.println("    -h : print help");
    }

    private static ShohousenData toData(ShohousenInput input) {
        ShohousenData data = new ShohousenData();
        data.clinicAddress = input.clinicAddress;
        data.clinicName = input.clinicName;
        data.clinicPhone = input.clinicPhone;
        data.kikancode = input.kikancode;
        data.doctorName = input.doctorName;
        data.hokenshaBangou = input.hokenshaBangou;
        data.hihokensha = input.hihokensha;
        data.futansha = input.futansha;
        data.jukyuusha = input.jukyuusha;
        data.futansha2 = input.futansha2;
        data.jukyuusha2 = input.jukyuusha2;
        data.shimei = input.shimei;
        data.birthday = input.birthday != null ? LocalDate.parse(input.birthday) : null;
        data.sex = input.sex;
        data.honnin = input.honnin;
        data.futanWari = input.futanWari;
        data.koufuDate = input.koufuDate != null ? LocalDate.parse(input.koufuDate) : null;
        if(  input.validUptoDate != null && !input.validUptoDate.equals("") ){
            data.validUptoDate = LocalDate.parse(input.validUptoDate);
        }
        data.setDrugs(input.content);
        data.pharmacyName = input.pharmacyName;
        return data;
    }

}
