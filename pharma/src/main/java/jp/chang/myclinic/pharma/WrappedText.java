package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

class WrappedText extends JPanel {

    private int width;
    private int ascent;
    private int fontSize;
    private List<Item> items = new ArrayList<>();
    private int posX;
    private int posY;
    private int botY;

    public WrappedText(int width){
        setLayout(null);
        this.width = width;
        Font font = getFont();
        this.ascent = getFontMetrics(font).getAscent();
        this.fontSize = font.getSize();
        this.botY = this.fontSize;
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point p = e.getPoint();
                System.out.println(p);
            }
        });
    }

    public WrappedText(String text, int width){
        this(width);
        appendString(text);
    }

    private List<StringItem> breakToLines(String text){
        List<StringItem> result = new ArrayList<>();
        FontMetrics fm = getFontMetrics(getFont());
        StringBuilder sb = new StringBuilder();
        int x = posX;
        for(char ch: text.toCharArray()){
            if( ch == '\n' ){
                result.add(new StringItem(posX, posY + ascent, sb.toString()));
                sb.setLength(0);
                newline();
                x = 0;
                continue;
            }
            int cw = fm.charWidth(ch);
            if( x > 0 && x + cw > width ){
                result.add(new StringItem(posX, posY + ascent, sb.toString()));
                sb.setLength(0);
                newline();
                x = 0;
            }
            sb.append(ch);
            x += cw;
        }
        if( sb.length() > 0 ){
            result.add(new StringItem(posX, posY + ascent, sb.toString()));
            posX = x;
        }
        return result;
    }

    private void appendString(String text){
        items.addAll(breakToLines(text));
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

    public void appendLink(String text, Runnable action){
        List<LinkItem> linkItems = breakToLines(text).stream()
                .map(stringItem -> new LinkItem(stringItem, action))
                .collect(Collectors.toList());
        items.addAll(linkItems);
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
        for(Item item: items) {
            item.render(g);
        }
        paintComponents(g);
    }

    private interface Item {
        void render(Graphics g);
    }

    private static class StringItem implements Item {
        int x;
        int y;
        String text;

        StringItem(int x, int y, String text){
            this.x = x;
            this.y = y;
            this.text = text;
        }

        @Override
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

    private static class LinkItem implements Item {
        StringItem stringItem;
        Runnable runnable;

        LinkItem(StringItem stringItem, Runnable runnable){
            this.stringItem = stringItem;
            this.runnable = runnable;
        }

        @Override
        public void render(Graphics g){
            Color colorSave = g.getColor();
            g.setColor(Color.BLUE);
            stringItem.render(g);
            g.setColor(colorSave);
        }

        @Override
        public String toString() {
            return "LinkItem{" +
                    "stringItem=" + stringItem +
                    ", runnable=" + runnable +
                    '}';
        }
    }

}
