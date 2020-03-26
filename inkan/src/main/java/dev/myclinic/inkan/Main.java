package dev.myclinic.inkan;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Main {

    public static void main(String[] args){
        String filePath = args[0];
        Document document = new Document();
        try {
            PdfReader reader = new PdfReader(filePath);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("work/test.pdf"));
            Image img = Image.getInstance("config/hanko.png");
            img.scalePercent(65);
            img.setAbsolutePosition(340, 714);
            stamper.getOverContent(1).addImage(img);
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
