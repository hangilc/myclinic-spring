package jp.chang.myclinic.pharma.wrappedtext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Line {
    private int left;
    private int top;
    private int width;
    private int height;
    private int baseLineOffset;
    private int currentWidth;
    private List<Item> items = new ArrayList<>();

    public Line(int left, int top, int width){
        this.left = left;
        this.top = top;
        this.width = width;
    }

    public boolean addChar(char ch, VAlign valign, FontMetrics fontMetrics){
        int cw = fontMetrics.charWidth(ch);;
        if( currentWidth > 0 ){
            if (currentWidth + cw > width) {
                return false;
            }
        }
        int ascent = fontMetrics.getAscent();
        int fontHeight = fontMetrics.getHeight();
        switch(valign){
            case Top:case Center:case Bottom: {
                if( height < fontHeight ){
                    height = fontHeight;
                }
                break;
            }
            case BaseLine: {
                if( baseLineOffset < ascent ){
                    baseLineOffset = ascent;
                }
                if( height - baseLineOffset < fontHeight - ascent ){
                    height = baseLineOffset + (fontHeight - ascent);
                }
                break;
            }
        }
        items.add(new CharItem(ch, cw, valign, fontHeight, ascent));
    }

    public boolean addComponent(JComponent component, VAlign valign){
        int w = component.getWidth();
        int h = component.getHeight();
        if( currentWidth > 0 ){
            if( currentWidth + w > width ){
                return false;
            }
        }
        switch(valign){
            case Top: {
                if( height < h ){
                    height = h;
                }
                break;
            }
            case Center: {
                if( height < h ){
                    baseLineOffset += (h - height) / 2;
                    height = h;
                }
                break;
            }
            case Bottom: {
                if( height < h ){
                    baseLineOffset += h - height;
                    height = h;
                }
                break;
            }
            case BaseLine: throw new RuntimeException("invalid valign (BaseLine) for addComponent");
        }
    }

    public void render(Graphics g){
        int x = left;
        for(Item item: items){
            x = item.render(g, x);
        }
    }

    public enum VAlign {
        Top, Center, BaseLine, Bottom
    }

    private interface Item {
        int render(Graphics g, int x);
    }

    private class CharItem implements Item {
        char ch;
        int charWidth;
        VAlign valign;
        int fontHeight;
        int ascent;

        CharItem(char ch, int charWidth, VAlign valign, int fontHeight, int ascent){
            this.ch = ch;
            this.charWidth = charWidth;
            this.valign = valign;
            this.fontHeight = fontHeight;
            this.ascent = ascent;
        }

        int render(Graphics g, int x){
            int y;
            switch(valign){
                case Top: y = top + ascent; break;
                case Center: y = (int)(top + height/2.0 - fontHeight/2.0 + ascent); break;
                case BaseLine: y = top + baseLineOffset; break;
                case Bottom: y = top + height - fontHeight + ascent; break;
            }
            g.drawChars(new char[]{ ch }, 0, 1, x, y);
            return x + charWidth;
        }
    }

    private class ComponentItem implements Item {
        JComponent component ;
        VAlign valign;

        ComponentItem(JComponent component, VAlign valign){
            this.component = component;
            this.valign = valign;
        }

        public int render(Graphics g, int x){
            int h = component.getHeight();
            switch(valign){
                case Top: {

                }
            }
        }
    }
}
