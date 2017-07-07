package jp.chang.myclinic.pharma.wrappedtext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
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

    public boolean isEmpty(){
        return currentWidth > 0;
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
                if( height < fontHeight ){
                    height = fontHeight;
                }
                if( baseLineOffset < ascent ){
                    baseLineOffset = ascent;
                }
                break;
            }
        }
        items.add(new CharItem(ch, cw, valign, fontHeight, ascent));
        currentWidth += cw;
        return true;
    }

    public boolean addLink(char ch, VAlign valign, FontMetrics fontMetrics, Runnable action){
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
                if( height < fontHeight ){
                    height = fontHeight;
                }
                if( baseLineOffset < ascent ){
                    baseLineOffset = ascent;
                }
                break;
            }
        }
        items.add(new LinkItem(ch, cw, valign, fontHeight, ascent, action));
        currentWidth += cw;
        return true;
    }

    public boolean addComponent(JComponent component, VAlign valign){
        Dimension dim = component.getPreferredSize();
        int w = (int)dim.getWidth();
        int h = (int)dim.getHeight();
        int y = top;
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
                y = top;
                break;
            }
            case Center: {
                if( height < h ){
                    baseLineOffset += (h - height) / 2;
                    height = h;
                }
                y = top + (height - h)/2;
                break;
            }
            case Bottom: {
                if( height < h ){
                    baseLineOffset += h - height;
                    height = h;
                }
                y = top + height - h;
                break;
            }
            case BaseLine: throw new RuntimeException("invalid valign (BaseLine) for addComponent");
        }
        component.setBounds(left + currentWidth, y, (int)dim.getWidth(), (int)dim.getHeight());
        currentWidth += w;
        return true;
    }

    public void render(Graphics g){
        int x = left;
        for(Item item: items){
            x = item.render(g, x);
        }
    }

    public int getTop(){
        return top;
    }

    public int getHeight(){
        return height;
    }

    public boolean containsPoint(int x, int y){
        return y >= top && y < top + height && x >= left && x < left + width;
    }

    public void handleClick(int x, int y){
        int probeX = left;
        for(Item item: items){
            if( probeX <= x && x < probeX + item.getCharWidth() ){
                if( item instanceof LinkItem ){
                    LinkItem linkItem = (LinkItem)item;
                    linkItem.run();
                }
                return;
            } else {
                probeX += item.getCharWidth();
            }
         }
    }

    public enum VAlign {
        Top, Center, BaseLine, Bottom
    }

    private interface Item {
        int render(Graphics g, int x);
        int getCharWidth();
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

        @Override
        public int render(Graphics g, int x){
            int y = top;
            switch(valign){
                case Top: y = top + ascent; break;
                case Center: y = (int)(top + height/2.0 - fontHeight/2.0 + ascent); break;
                case BaseLine: y = top + baseLineOffset; break;
                case Bottom: y = top + height - fontHeight + ascent; break;
            }
            g.drawChars(new char[]{ ch }, 0, 1, x, y);
            return x + charWidth;
        }

        @Override
        public int getCharWidth(){
            return charWidth;
        }
    }

    private class LinkItem extends CharItem {

        private Runnable callback;

        LinkItem(char ch, int charWidth, VAlign valign, int fontHeight, int ascent, Runnable callback){
            super(ch, charWidth, valign, fontHeight, ascent);
            this.callback = callback;
        }

        @Override
        public int render(Graphics g, int x){
            Color save = g.getColor();
            g.setColor(Color.BLUE);
            int ret = super.render(g, x);
            g.setColor(save);
            return ret;
        }

        public void run(){
            callback.run();
        }
    }

}
