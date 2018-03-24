package jp.chang.myclinic.mastermanip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class Cat {

    private static Logger logger = LoggerFactory.getLogger(Cat.class);

    void run(String[] args){
        if( args.length == 2 ){
            String type = args[0];
            String zipFile = args[1];
            System.out.println("type: " + type);
            System.out.println("zipFile: " + zipFile);
            String file = resolveFile(type);
            doCat(zipFile, file);
        }
    }

    private void doCat(String zipFile, String file){
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
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to open zipfile: " + zipFile);
            System.exit(1);
        }
    }

    private String resolveFile(String type){
        switch(type){
            case "iyakuhin": return "y.csv";
            case "shinryou": return "s.csv";
            case "kizai": return "t.csv";
            default: {
                System.err.println("Unknown type. Types are: iyakuhin, shinryou, or kizai.");
                System.exit(1);
                return null;
            }
        }
    }

}
