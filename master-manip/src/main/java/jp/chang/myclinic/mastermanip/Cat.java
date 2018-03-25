package jp.chang.myclinic.mastermanip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Cat {

    private static Logger logger = LoggerFactory.getLogger(Cat.class);

    public static void main(String[] args) throws IOException {
        if( args.length == 2 ){
            String type = args[0];
            String zipFile = args[1];
            System.out.println("type: " + type);
            System.out.println("zipFile: " + zipFile);
            String file = resolveFile(type);
            cat(zipFile, file);
        } else {
            System.err.println("Usage: master-cat kind zipfile");
            System.exit(1);
        }

    }

    private static void cat(String zipFile, String file) throws IOException {
        try(ZipFile zip = new ZipFile(zipFile, Charset.forName("MS932"))){
            ZipEntry entry = zip.getEntry(file);
            if( entry == null ){
                System.err.println("Cannot find file: " + file);
                System.exit(1);
            } else {
                try(InputStream is = zip.getInputStream(entry)){
                    is.transferTo(System.out);
                }
            }
        }
    }

    private static String resolveFile(String type){
        switch(type){
            case "iyakuhin": return "y.csv";
            case "shinryou": return "s.csv";
            case "kizai": return "t.csv";
            default: {
                throw new RuntimeException("Unknown type. Types are: iyakuhin, shinryou, or kizai.");
            }
        }
    }

}
