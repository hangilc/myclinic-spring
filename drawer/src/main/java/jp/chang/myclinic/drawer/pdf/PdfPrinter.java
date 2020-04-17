package jp.chang.myclinic.drawer.pdf;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.OpCreateFont;
import jp.chang.myclinic.drawer.OpSetFont;

import java.io.FileOutputStream;
import java.util.List;

public class PdfPrinter {

    public PdfPrinter() {

    }

    private double milliToPoint(double milli){
        double inch = milli * 0.0393701;
        return inch / 72.0;
    }

    public void print(List<List<Op>> pages, String savePath) throws Exception{
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(savePath));
        doc.open();
        for(int i=0;i<pages.size();i++){
            List<Op> ops = pages.get(0);
            for(Op op: ops){
                switch(op.getOpCode()){
                    case CreateFont: {
                        OpCreateFont opCreateFont = (OpCreateFont)op;
                        double size = milliToPoint(opCreateFont.getSize());
                        PdfFont font = PdfFontFactory.createFont("C:\\Windows\\Fonts\\msmincho.ttc,0", "Identity-H");
                        System.out.println(font);
                        break;
                    }
//                default:
//                    throw new RuntimeException("Unknown op");
                }
            }
            break;
        }
        doc.add(new Paragraph("Hello, world"));
        doc.close();
    }

}
