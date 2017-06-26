package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

class WrappedText extends JPanel {

    private int width;
    private int ascent;
    private int fontSize;
    private List<StringItem> stringItems = new ArrayList<>();
    private int posX;
    private int posY;
    private int botY;
    private Font defaultFont;

    public WrappedText(int width){
        setLayout(null);
        this.width = width;
        Font font = getFont();
        this.ascent = getFontMetrics(font).getAscent();
        this.fontSize = font.getSize();
        this.botY = this.fontSize;
        defaultFont = getFont();
    }

    public WrappedText(String text, int width){
        this(width);
        appendString(text);
    }

    public void appendString(String text){
        FontMetrics fm = getFontMetrics(getFont());
        StringBuilder sb = new StringBuilder();
        int x = posX;
        for(char ch: text.toCharArray()){
            if( ch == '\n' ){
                stringItems.add(new StringItem(posX, posY, sb.toString()));
                sb.setLength(0);
                newline();
                x = 0;
                continue;
            }
            int cw = fm.charWidth(ch);
            if( x > 0 && x + cw > width ){
                stringItems.add(new StringItem(posX, posY, sb.toString()));
                sb.setLength(0);
                newline();
                x = 0;
            }
            sb.append(ch);
            x += cw;
        }
        if( sb.length() > 0 ){
            stringItems.add(new StringItem(posX, posY, sb.toString()));
            posX = x;
        }
        setAllSizes();
    }

    public void appendComponent(JComponent component){
        Dimension dim = component.getPreferredSize();
        if( posX > 0 && dim.getWidth() + posX > width ){
            newline();
        }
        component.setBounds(posX, posY, (int)dim.getWidth(), (int)dim.getHeight());
        posX += (int)dim.getWidth();
        int y = posY + (int)dim.getHeight();
        if( y > botY ){
            botY = y;
        }
        add(component);
        setAllSizes();
    }

    private void setAllSizes(){
        Dimension size = new Dimension(this.width, getCurrentHeight());
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    private int getCurrentHeight(){
        return botY;
    }

    private void newline(){
        posX = 0;
        posY = botY;
        botY = posY + fontSize;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(StringItem item: stringItems){
            g.drawString(item.text, item.x, item.y + ascent);
        }
        paintComponents(g);
    }

    private static class StringItem {
        int x;
        int y;
        String text;

        StringItem(int x, int y, String text){
            this.x = x;
            this.y = y;
            this.text = text;
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

//    private interface Src {
//        void append();
//    }
//
//    private class SrcString implements Src {
//        private String text;
//
//        public SrcString(String src){
//            this.text = src;
//        }
//
//        public void append(){
//            FontMetrics fm = getFontMetrics(getFont());
//            StringBuilder sb = new StringBuilder();
//            int x = posX;
//            for(char ch: text.toCharArray()){
//                if( ch == '\n' ){
//                    stringItems.add(new StringItem(posX, posY, sb.toString()));
//                    sb.setLength(0);
//                    newline();
//                    x = 0;
//                    continue;
//                }
//                int cw = fm.charWidth(ch);
//                if( x > 0 && x + cw > width ){
//                    stringItems.add(new StringItem(posX, posY, sb.toString()));
//                    sb.setLength(0);
//                    newline();
//                    x = 0;
//                }
//                sb.append(ch);
//                x += cw;
//            }
//            if( sb.length() > 0 ){
//                stringItems.add(new StringItem(posX, posY, sb.toString()));
//                posX = x;
//            }
//        }
//    }
//
//    private class SrcComponent implements Src {
//        private JComponent component;
//
//        public SrcComponent(JComponent component){
//            this.component = component;
//            add(component);
//        }
//
//        public void append(){
//            Dimension dim = component.getPreferredSize();
//            if( posX > 0 && dim.getWidth() + posX > width ){
//                newline();
//            }
//            component.setBounds(posX, posY, (int)dim.getWidth(), (int)dim.getHeight());
//            posX += (int)dim.getWidth();
//            int y = posY + (int)dim.getHeight();
//            if( y > botY ){
//                botY = y;
//            }
//        }
//    }

}
