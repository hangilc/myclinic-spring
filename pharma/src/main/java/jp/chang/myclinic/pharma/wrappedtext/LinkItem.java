package jp.chang.myclinic.pharma.wrappedtext;

import java.awt.*;

public class LinkItem {
    private StringItem stringItem;
    private Runnable runnable;

    public LinkItem(StringItem stringItem, Runnable runnable){
        this.stringItem = stringItem;
        this.runnable = runnable;
    }

    public void render(Graphics g){
        //Color colorSave = g.getColor();
        //g.setColor(Color.BLUE);
        stringItem.render(g);
        //g.setColor(colorSave);
    }

    @Override
    public String toString() {
        return "LinkItem{" +
                "stringItem=" + stringItem +
                ", runnable=" + runnable +
                '}';
    }
}
