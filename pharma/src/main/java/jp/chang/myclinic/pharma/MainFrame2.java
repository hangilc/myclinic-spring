package jp.chang.myclinic.pharma;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame2 extends JFrame {

    JPanel myPanel;

    MainFrame2(){
        JScrollPane sp = new JScrollPane(createPanel());
        add(sp);
        setPreferredSize(new Dimension(200, 200));
        pack();
        myPanel.repaint();
        myPanel.revalidate();
    }

    JPanel createPanel(){
        JPanel panel = new MyPanel();
        myPanel = panel;
        panel.setLayout(new MigLayout("fill, debug", "[]6px[]", ""));
        panel.add(new WrappedText2("Hello, world, Hello, world, Hello, world, Hello, world", 0), "w 48%, wmin 10");
        {
            WrappedText2 wt = new WrappedText2("Hello, world, Hello, world, Hello, world, Hello, world", 0);
            wt.append(new JButton("click"));
            System.out.println("wt size: " + wt.getPreferredSize());
            panel.add(wt, "w 48%, wmin 10, wrap");
        }
        panel.add(new WrappedText2("Hello, world, Hello, world, Hello, world, Hello, world", 0), "w 48%, wmin 10");
        panel.add(new WrappedText2("Hello, world, Hello, world, Hello, world, Hello, world", 0), "w 48%, wmin 10, wrap");
        return panel;
    }
}

class MyPanel extends JPanel implements Scrollable {

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return null;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 0;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}

class Component2 extends JPanel {

    private int width = 10;
    private int height = 10;

    Component2(){
        setBackground(Color.BLUE);
    }

    @Override
    public Dimension getPreferredSize(){
        System.out.println("getPreferredSize: " + width + "," + height);
        return new Dimension(width, height);
    }

    @Override
    public void setSize(int width, int height) {
        System.out.println("setSize: " + width + "," + height);
        super.setSize(width, height);
    }

    @Override
    public void setSize(Dimension size){
        System.out.println("setSize: " + size);
        super.setSize(size);
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        System.out.printf("setBounds: %d, %d, %d, %d\n", x, y, width, height);
        super.setBounds(x, y, width, height);
        this.width = width;
        this.height = width / 10;
        if( this.width != width || this.height != height ){
            EventQueue.invokeLater(() -> {
                repaint();
                revalidate();
            });
        }
    }
}

class WrappedText2 extends JPanel {

    private int width;
    private int ascent;
    private int fontSize;
    private List<Src> sources = new ArrayList<>();
    private List<StringItem> stringItems = new ArrayList<>();
    private int posX;
    private int posY;
    private int botY;

    public WrappedText2(String text, int width){
        setLayout(null);
        this.width = width;
        Font font = getFont();
        this.ascent = getFontMetrics(font).getAscent();
        this.fontSize = font.getSize();
        this.botY = this.fontSize;
        System.out.println("fontSize: " + this.fontSize);
        append(text);
    }

    public void append(String text){
        Src src = new SrcString(text);
        src.append();
        sources.add(src);
    }

    public void append(JComponent component){
        Src src = new SrcComponent(component);
        src.append();
        sources.add(src);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, getCurrentHeight());
    }

    @Override
    public void setBounds(int x, int y, int width, int height){
        super.setBounds(x, y, width, height);
        System.out.println("setBounds: " + width + "," + height);
        if( this.width != width || getCurrentHeight() != height ){
            this.width = width;
            this.posX = 0;
            this.posY = 0;
            this.botY = fontSize;
            stringItems.clear();
            for(Src src: sources){
                src.append();
            }
            repaint();
            revalidate();
        }
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

    private interface Src {
        void append();
    }

    private class SrcString implements Src {
        private String text;

        public SrcString(String src){
            this.text = src;
        }

        public void append(){
            FontMetrics fm = getFontMetrics(getFont());
            StringBuilder sb = new StringBuilder();
            System.out.println("fontSize (append): " + getFont().getSize());
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
            System.out.println("stringItems: " + stringItems);
        }
    }

    private class SrcComponent implements Src {
        private JComponent component;

        public SrcComponent(JComponent component){
            this.component = component;
            add(component);
        }

        public void append(){
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
        }
    }

}


