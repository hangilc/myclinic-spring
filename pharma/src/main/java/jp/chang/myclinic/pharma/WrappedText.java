package jp.chang.myclinic.pharma;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangil on 2017/06/11.
 */
public class WrappedText extends JPanel {

    private int width;
    private int ascent;
    private int fontSize;
    private List<StringItem> stringItems = new ArrayList<>();
    private int posX;
    private int posY;
    private int botY;

    public WrappedText(int width, String text){
        setLayout(null);
        this.width = width;
        Font font = getFont();
        this.ascent = getFontMetrics(font).getAscent();
        this.fontSize = font.getSize();
        this.botY = this.fontSize;
        append(text);
    }

    private void append(String text){
        FontMetrics fm = getFontMetrics(getFont());
        StringBuilder sb = new StringBuilder();
        int x = posX;
        for(char ch: text.toCharArray()){
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
    }

    public void append(JComponent component){
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
    }

    private void newline(){
        posX = 0;
        posY = botY;
        botY = posY + fontSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, botY);
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
    }

}

