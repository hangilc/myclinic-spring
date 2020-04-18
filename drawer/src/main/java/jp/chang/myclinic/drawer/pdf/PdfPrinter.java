package jp.chang.myclinic.drawer.pdf;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import jp.chang.myclinic.drawer.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfPrinter {

    private double paperWidth;
    private double paperHeight;
    private boolean inText;
    private float ascent;

    public PdfPrinter() {
        this(jp.chang.myclinic.drawer.PaperSize.A4);
    }

    public PdfPrinter(jp.chang.myclinic.drawer.PaperSize paperSize){
        this(paperSize.getWidth(), paperSize.getHeight());
    }

    public PdfPrinter(double paperWidth, double paperHeight){
        this.paperWidth = milliToPoint(paperWidth);
        this.paperHeight = milliToPoint(paperHeight);
    }

    private double milliToPoint(double milli) {
        double inch = milli * 0.0393701;
        return inch * 72.0;
    }

    private static class FontResourceData {
        public String location;
        public String encoding;

        public FontResourceData(String location, String encoding) {
            this.location = location;
            this.encoding = encoding;
        }
    }

    private static final Map<String, FontResourceData> fontResourceMap = new HashMap<>();

    static {
        FontResourceData mincho = new FontResourceData("C:\\Windows\\Fonts\\msmincho.ttc,0", "Identity-H");
        fontResourceMap.put("MS Mincho", mincho);
        FontResourceData gothic = new FontResourceData("C:\\Windows\\Fonts\\msgothic.ttc,0", "Identity-H");
        fontResourceMap.put("MS Gothic", gothic);
    }

//    private static BaseFont mincho;
//
//    static {
//        try {
//            mincho = BaseFont.createFont("C:\\Windows\\Fonts\\msmincho.ttc,0", "Identity-H",
//                    BaseFont.EMBEDDED);
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }

    private static class DrawerFont {
        public BaseFont font;
        public float size;

        public DrawerFont(BaseFont font, float size) {
            this.font = font;
            this.size = size;
        }

    }

    private float getX(double milliX) {
        double pointX = milliToPoint(milliX);
        return (float)pointX;
    }

    private float getY(double milliY){
        double pointY = milliToPoint(milliY);
        return (float)(paperHeight - pointY);
    }

    public void print(List<List<Op>> pages, String savePath) throws Exception {
        Map<String, BaseFont> fontMap = new HashMap<>();
        Map<String, DrawerFont> drawerFontMap = new HashMap<>();
        List<Runnable> textProlog = new ArrayList<>();
        Document doc = new Document(new Rectangle((float)paperWidth, (float)paperHeight), 0, 0, 0, 0);
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(savePath));
        doc.open();
        PdfContentByte cb = pdfWriter.getDirectContent();
        for (int i = 0; i < pages.size(); i++) {
            if (i != 0) {
                doc.newPage();
            }
            doc.add(new Chunk(""));
            List<Op> ops = pages.get(i);
            for (Op op : ops) {
                switch (op.getOpCode()) {
                    case CreateFont: {
                        OpCreateFont opCreateFont = (OpCreateFont) op;
                        String name = opCreateFont.getName();
                        if (!drawerFontMap.containsKey(name)) {
                            String fontName = opCreateFont.getFontName();
                            if (!fontMap.containsKey(fontName)) {
                                FontResourceData fr = fontResourceMap.getOrDefault(fontName, null);
                                if (fr == null) {
                                    throw new RuntimeException("Cannot find font: " + fontName);
                                }
                                BaseFont font = BaseFont.createFont(fr.location, fr.encoding,
                                        BaseFont.EMBEDDED);
                                fontMap.put(fontName, font);
                            }
                            BaseFont font = fontMap.get(fontName);
                            float size = (float) milliToPoint(opCreateFont.getSize());
                            drawerFontMap.put(name, new DrawerFont(font, size));
                        }
                        break;
                    }
                    case SetFont: {
                        OpSetFont opSetFont = (OpSetFont) op;
                        String name = opSetFont.getName();
                        DrawerFont dfont = drawerFontMap.getOrDefault(name, null);
                        if (dfont == null) {
                            throw new RuntimeException("Unknown font: " + name);
                        }
                        if (inText) {
                            cb.setFontAndSize(dfont.font, dfont.size);
                            ascent = dfont.size;
                        } else {
                            BaseFont font = dfont.font;
                            float size = dfont.size;
                            textProlog.add(() -> {
                                cb.setFontAndSize(font, size);
                                ascent = size;
                            });
                        }
                        System.out.println(dfont.size);
                        System.out.println(dfont.font.getAscentPoint("国", dfont.size));
                        System.out.println(dfont.font.getAscentPoint("処", dfont.size));
                        break;
                    }
                    case DrawChars: {
                        if( !inText ){
                            cb.beginText();
                            textProlog.forEach(Runnable::run);
                            textProlog.clear();
                            inText = true;
                        }
                        OpDrawChars opDrawChars = (OpDrawChars) op;
                        String chars = opDrawChars.getChars();
                        List<Double> xs = opDrawChars.getXs();
                        List<Double> ys = opDrawChars.getYs();
                        for(int j=0;j<chars.length();j++){
                            String text = chars.substring(j, j+1);
                            double x = j < xs.size() ? xs.get(j) : xs.get(xs.size() - 1);
                            double y = j < ys.size() ? ys.get(j) : ys.get(ys.size() - 1);
                            cb.setTextMatrix(getX(x), getY(y) - ascent);
                            cb.showText(text);
                        }
                        break;
                    }
//                default:
//                    throw new RuntimeException("Unknown op");
                }
            }
            if (inText) {
                cb.endText();
                inText = false;
            }
        }
        doc.close();
    }

}
