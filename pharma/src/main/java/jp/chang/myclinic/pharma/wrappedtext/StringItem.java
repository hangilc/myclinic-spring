package jp.chang.myclinic.pharma.wrappedtext;

import java.awt.*;

public class StringItem {
    private int x;
    private int y;
    private String text;

    public StringItem(int x, int y, String text){
        this.x = x;
        this.y = y;
        this.text = text;
    }

    public void render(Graphics g){
        g.drawString(this.text, this.x, this.y);
    }

    @Override
    public String toString() {
        return "StringItem{" +
                "x=" + x +
                ", y=" + y +
                ", text='" + text + '\'' +
                '}';
    }
}
