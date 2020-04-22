package jp.chang.myclinic.multidrawer.paragraph;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.multidrawer.DataDrawer;

import java.util.ArrayList;
import java.util.List;

public class ParagraphDrawer implements DataDrawer<ParagraphData> {

    private Insets insets = new Insets(20);
    private PaperSize paperSize = PaperSize.A4;

    @Override
    public List<List<Op>> draw(ParagraphData data) {
        List<List<Op>> pages = new ArrayList<>();
        DrawerCompiler c = new DrawerCompiler();
        Box box = getContentArea();
        data.getLines().forEach(line -> {
        });
        pages.add(c.getOps());
        return pages;
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

}
