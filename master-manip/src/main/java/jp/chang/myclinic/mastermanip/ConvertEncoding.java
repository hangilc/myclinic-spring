package jp.chang.myclinic.mastermanip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ConvertEncoding {

    private static Logger logger = LoggerFactory.getLogger(ConvertEncoding.class);

    public static void main(String[] args) throws IOException {
        if( args.length == 2 ){
            String fromEnc = args[0];
            String toEnc = args[1];
            Charset toEncCharset = Charset.forName(toEnc);
            String lineLimit = "\r\n";
            if( toEncCharset == StandardCharsets.UTF_8 ){
                lineLimit = "\n";
            }
            forEachLine(fromEnc, toEncCharset, lineLimit);
        } else {
            System.err.println("Usage: convert-encoding from to");
            System.exit(1);
        }
    }

    private static void forEachLine(String fromEnc, Charset toEnc, String lineLimit) throws IOException {
        try (InputStreamReader inputStreamReader = new InputStreamReader(System.in, fromEnc)) {
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                while(true){
                    String line = reader.readLine();
                    if( line == null ){
                        break;
                    } else {
                        byte[] bytes = line.getBytes(toEnc);
                        System.out.write(bytes);
                        System.out.print(lineLimit);
                    }
                }
            }
        }
    }

}
