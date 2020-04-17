package jp.chang.myclinic.drawerprinter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpDeserializer;
import jp.chang.myclinic.drawer.Op;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {

    private static class CmdArgs {
        public String input;

        public CmdArgs(String[] args){
            for(int i=0;i<args.length;i++){
                String arg = args[i];
                switch(arg){
                    case "-i": case "--in": {
                        this.input = args[i+1];
                        i += 1;
                        break;
                    }
                    default: {
                        usage();
                        System.exit(1);
                    }
                }
            }
        }

        public static void usage(){
            System.err.println("Usage: drawer-printer [options] [INPUT]");
            System.err.println("  options:");
            System.err.println("    -i, --in INPUT: read from file");
        }
    }

    public static void main(String[] args) throws IOException {
        CmdArgs cmdArgs = new CmdArgs(args);
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Op.class, new JacksonOpDeserializer());
        mapper.registerModule(module);
        List<List<Op>> pages = null;
        InputStreamReader reader = null;
        if( cmdArgs.input == null ){
            reader = new InputStreamReader(System.in, Charset.defaultCharset());
        } else {
            System.out.println(cmdArgs.input);
            InputStream fin = new FileInputStream(cmdArgs.input);
            reader = new InputStreamReader(fin, StandardCharsets.UTF_8);
        }
        pages = mapper.readValue(reader, new TypeReference<List<List<Op>>>(){});
        reader.close();
        System.out.println(pages);
    }

    private Main(List<List<Op>> pages){
        System.out.println(pages);
    }
}
