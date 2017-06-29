package jp.chang.myclinic.pharma.wrappedtext;

import java.awt.*;

public class LinkItem {
    private StringItem stringItem;
    private Rectangle rect;
    private Runnable runnable;

    public LinkItem(StringItem stringItem, Rectangle rect, Runnable runnable){
        this.stringItem = stringItem;
        this.rect = rect;
        this.runnable = runnable;
    }

    public void render(Graphics g){
        stringItem.render(g);
    }

    public boolean contains(Point p){
        return rect.contains(p);
    }

    public void doAction(){
        runnable.run();
    }

    @Override
    public String toString() {
        return "LinkItem{" +
                "stringItem=" + stringItem +
                ", runnable=" + runnable +
                '}';
    }
}
