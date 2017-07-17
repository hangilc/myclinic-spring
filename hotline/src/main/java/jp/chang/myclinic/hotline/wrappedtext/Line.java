package jp.chang.myclinic.hotline.wrappedtext;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Line {
    private int left;
    private int contentWidth;
    private int width;
    private int top;
    private int height;
    private int baseLineOffset;
    private List<Item> items = new ArrayList<>();

    public Line(int left, int top, int width){
        this.left = left;
        this.top = top;
        this.width = width;
    }

    public boolean isEmpty(){
        return  items.size() == 0;
    }

    public int getRemaining(){
        return width - contentWidth;
    }

    public void addString(String text, int width, VAlign valign, int fontHeight, int ascent){
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
        items.add(new StringItem(text, width, valign, fontHeight, ascent));
        contentWidth += width;
    }

    public void addLink(String text, int width, VAlign valign, int fontHeight, int ascent, Runnable action){
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
        items.add(new LinkItem(text, width, valign, fontHeight, ascent, action));
        contentWidth += width;
    }

    public void addComponent(JComponent component, VAlign valign){
        Dimension dim = component.getPreferredSize();
        int w = (int)dim.getWidth();
        int h = (int)dim.getHeight();
        int y = top;
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
        component.setBounds(left + contentWidth, y, (int)dim.getWidth(), (int)dim.getHeight());
        items.add(new ComponentItem(w));
        contentWidth += w;
    }

    public void render(Graphics g){
        int x = left;
        for(Item item: items){
            item.renderItem(g, x);
            x += item.getItemWidth();
        }
    }

    public int getTop(){
        return top;
    }

    public int getHeight(){
        return height;
    }

    public boolean containsPoint(int x, int y){
        return y >= top && y < top + height && x >= left && x < left + contentWidth;
    }

    public void handleClick(int x, int y){
        int probeX = left;
        for(Item item: items){
            if( probeX <= x && x < probeX + item.getItemWidth() ) {
                if (item instanceof LinkItem) {
                    LinkItem linkItem = (LinkItem) item;
                    linkItem.run();
                }
                return;
            } else {
                probeX += item.getItemWidth();
            }
            if( probeX > x ){
                return;
            }
         }
    }

    public boolean isInLink(int x, int y){
        int probeX = left;
        for(Item item: items){
            if( probeX <= x && x < probeX + item.getItemWidth() ){
                return item instanceof LinkItem;
            } else {
                probeX += item.getItemWidth();
            }
            if( probeX > x ){
                break;
            }
        }
        return false;
    }

    public enum VAlign {
        Top, Center, BaseLine, Bottom
    }

    private interface Item {
        void renderItem(Graphics g, int x);
        int getItemWidth();
    }

    private class StringItem implements Item {

        private String text;
        private int itemWidth;
        private VAlign valign;
        private int fontHeight;
        private int ascent;

        StringItem(String text, int itemWidth, VAlign valign, int fontHeight, int ascent){
            this.text = text;
            this.itemWidth = itemWidth;
            this.valign = valign;
            this.fontHeight = fontHeight;
            this.ascent = ascent;
        }

        @Override
        public void renderItem(Graphics g, int x){
            int y = top;
            switch(valign){
                case Top: y = top + ascent; break;
                case Center: y = (int)(top + height/2.0 - fontHeight/2.0 + ascent); break;
                case BaseLine: y = top + baseLineOffset; break;
                case Bottom: y = top + height - fontHeight + ascent; break;
            }
            g.drawString(text, x, y);
        }

        @Override
        public int getItemWidth(){
            return itemWidth;
        }
    }

    private class LinkItem extends StringItem {

        private Runnable callback;

        LinkItem(String text, int itemWidth, VAlign valign, int fontHeight, int ascent, Runnable callback){
            super(text, itemWidth, valign, fontHeight, ascent);
            this.callback = callback;
        }

        @Override
        public void renderItem(Graphics g, int x){
            Color save = g.getColor();
            g.setColor(Color.BLUE);
            super.renderItem(g, x);
            g.setColor(save);
        }

        public void run(){
            callback.run();
        }
    }

    private class ComponentItem implements Item {
        private int itemWidth;

        ComponentItem(int itemWidth){
            this.itemWidth = itemWidth;
        }

        @Override
        public void renderItem(Graphics g, int x){
            // nop
        }

        @Override
        public int getItemWidth(){
            return itemWidth;
        }
    }

}
