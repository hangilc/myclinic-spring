package jp.chang.myclinic.multidrawer.text;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.multidrawer.DataDrawer;

import java.util.ArrayList;
import java.util.List;

public class TextDrawer implements DataDrawer<String> {

    private Insets insets = new Insets(20);
    private PaperSize paperSize = PaperSize.A4;
    private String fontName = "MS Gothic";
    private double fontSize = 4;
    private double leading = 1;

    @Override
    public List<List<Op>> draw(String data) {
        List<List<Op>> pages = new ArrayList<>();
        DrawerCompiler c = new DrawerCompiler();
        setupPage(c);
        Box box = getContentArea();
        List<String> lines = new ArrayList<>();
        for(String line: splitToLines(data)){
            lines.addAll(c.breakLine(line, box.getWidth()));
        }
        while( true ){
            DrawerCompiler.MultiTextResult result = c.tryMultilineText(
                    lines, box, DrawerCompiler.HAlign.Left, DrawerCompiler.VAlign.Top, leading);
            if( result.allDrawn ){
                break;
            } else {
                pages.add(c.getOps());
                c.clearOps();
                setupPage(c);
                lines = lines.subList(result.linesRendered, lines.size());
                box = getContentArea();
            }
        }
        pages.add(c.getOps());
        return pages;
    }

    private void setupPage(DrawerCompiler c){
        c.createFont("default", fontName, fontSize);
        c.setFont("default");
    }

    private Box getContentArea(){
        Box box = new Box(paperSize);
        return box.inset(insets.insetLeft, insets.insetTop, insets.insetRight, insets.insetBottom);
    }

    public void setPaperSize(PaperSize paperSize){
        this.paperSize = paperSize;
    }

    public PaperSize getPaperSize(){
        return paperSize;
    }

    public void setInsets(double inset){
        this.insets = new Insets(inset);
    }

    public void setInsets(double left, double top, double right, double bottom){
        this.insets = new Insets(left, top, right, bottom);
    }

    public Insets getInsets(){
        return insets;
    }

    public void setFont(String fontName, double size){
        String fn = fontName.toLowerCase();
        switch(fn){
            case "ms gothic":
            case "gothic":
            case "sans-serif":{
                this.fontName = "MS Gothic";
                break;
            }
            case "ms mincho":
            case "mincho":
            case "serif":{
                this.fontName = "MS Mincho";
                break;
            }
            default:
                throw new RuntimeException("Unknown font name: " + fontName);
        }
        this.fontSize = size;
    }

    public void setLeading(double leading){
        this.leading = leading;
    }

    private String[] splitToLines(String line){
        if( line == null ){
            return new String[]{};
        } else {
            return line.split("\\r\\n|\\n|\\r");
        }
    }

}
