package dev.myclinic.inkan;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static void usage(){
        System.out.println("Usage inkan.jar [OPTIONS] unstamped.pdf\n" +
            "    -g stamp gray inkan\n" +
            "config/hanko.png is used to add stamp\n" +
            "(in case of -g, config/hanko-gray.png is used)");
    }

    private static Path filePath;
    private static boolean isGray = false;

    private static boolean parseArgs(String[] args){
        for (String arg : args) {
            if (arg == null) {
                return false;
            }
            if ("-g".equals(arg)) {
                isGray = true;
            } else {
                filePath = Paths.get(arg);
            }
        }
        return filePath != null;
    }

    public static void main(String[] args){
        if( !parseArgs(args) ){
            usage();
            System.exit(1);
        }
        Path stampPath;
        if( isGray ){
            stampPath = Paths.get(System.getenv("MYCLINIC_CONFIG"), "hanko-gray.png");
        } else {
            stampPath = Paths.get(System.getenv("MYCLINIC_CONFIG"), "hanko.png");
        }
        String outPath = filePath.toString().replace(".pdf", "-stamped.pdf");
        Document document = new Document();
        try {
            PdfReader reader = new PdfReader(filePath.toString());
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outPath));
            Image img = Image.getInstance(stampPath.toString());
            img.scalePercent(65);
            img.setAbsolutePosition(340, 708);
            stamper.getOverContent(1).addImage(img);
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
