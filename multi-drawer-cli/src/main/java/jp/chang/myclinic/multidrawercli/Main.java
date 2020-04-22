package jp.chang.myclinic.multidrawercli;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jp.chang.myclinic.drawer.JacksonOpSerializer;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawercli.seal8x3.Seal8x3Command;
import jp.chang.myclinic.multidrawercli.text.TextCommand;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    private final static Map<String, Command> commandMap = new LinkedHashMap<>();

    private static void usage(){
        System.err.println("Usage:multi-drawer-cli command args...");
        System.err.println("Usage:multi-drawer-cli [-h|--help] --- prints help");
        System.err.println("  commands:");
        for(String key: commandMap.keySet()){
            Command cmd = commandMap.get(key);
            System.err.printf("    %-8s --- %s\n", key, cmd.getDescription());
        }
        System.err.println("  common options:");
        System.err.println("    -i|--input INPUT) read from INPUT file");
        System.err.println("    -o|--output OUTPUT) write to OUTPUT file file");
        System.err.println("    --input-encoding ENCODING) ");
        System.err.println("    --output-encoding ENCODING) ");
    }

    private static class CommonArgs {
        String command;
        String input;
        String output;
        Charset inputEncoding;
        Charset outputEncoding;
        List<String> rest = new ArrayList<>();

        CommonArgs parse(String[] args){
            this.command = args[0];
            for(int i=1;i<args.length;i++){
                String arg = args[i];
                switch(arg){
                    case "-i":
                    case "--input":{
                        this.input = args[++i];
                        break;
                    }
                    case "-o":
                    case "--output":{
                        this.output = args[++i];
                        break;
                    }
                    case "--input-encoding":{
                        this.inputEncoding = Charset.forName(args[++i]);
                        break;
                    }
                    case "--output-encoding":{
                        this.outputEncoding = Charset.forName(args[++i]);
                        break;
                    }
                    default: {
                        this.rest.add(arg);
                        break;
                    }
                }
            }
            return this;
        }

        Charset resolveInputCharset(){
            if( inputEncoding != null ){
                return inputEncoding;
            } else if( input != null ){
                return StandardCharsets.UTF_8;
            } else {
                return Charset.defaultCharset();
            }
        }

        InputStream resolveInputStream() throws IOException {
            if( input == null ){
                return System.in;
            } else {
                return new FileInputStream(input);
            }
        }

        InputStreamReader getInputStreamReader() throws IOException {
            return new InputStreamReader(
                    resolveInputStream(),
                    resolveInputCharset()
            );
        }

        Charset resolveOutputCharset(){
            if( outputEncoding != null ){
                return outputEncoding;
            } else if( output != null ){
                return StandardCharsets.UTF_8;
            } else {
                return Charset.defaultCharset();
            }
        }

        OutputStream resolveOutputStream() throws IOException {
            if( output == null ){
                return System.out;
            } else {
                return new FileOutputStream(output);
            }
        }

        OutputStreamWriter getOutputStreamWriter() throws IOException {
            return new OutputStreamWriter(
                    resolveOutputStream(),
                    resolveOutputCharset()
            );
        }
    }

    public static void main(String[] args) throws Exception{
        commandMap.put("text", new TextCommand());
        commandMap.put("seal8x3", new Seal8x3Command());
        if( args.length == 0 ){
            usage();
            System.exit(1);
        } else if( args.length == 1 && "-h".equals(args[0]) || "--help".equals(args[0]) ){
            usage();
            System.exit(1);
        } else {
            CommonArgs commonArgs = new CommonArgs().parse(args);
            if( !commandMap.containsKey(commonArgs.command) ){
                System.err.println("Unknown command: " + commonArgs.command);
                usage();
                System.exit(1);
            }
            Command cmd = commandMap.get(commonArgs.command);
            cmd.initialize(commonArgs.rest);
            InputStreamReader reader = commonArgs.getInputStreamReader();
            BufferedReader bufReader = new BufferedReader(reader);
            Stream<String> source = bufReader.lines();
            List<List<Op>> pages = cmd.render(source);
            OutputStreamWriter writer = commonArgs.getOutputStreamWriter();
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addSerializer(Op.class, new JacksonOpSerializer());
            mapper.registerModule(module);
            String outputRep = mapper.writeValueAsString(pages);
            writer.write(outputRep);
            writer.flush();
        }
    }
}
