package jp.chang.myclinic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WrappedText extends JPanel {

    private String text;
    private int width;
    private int height;
    private List<String> lines;

    public WrappedText(String text, int width){
        setLayout(null);
        this.text = text;
        this.width = width;
        this.lines = breakToLines();
        this.height = getFont().getSize() * lines.size();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = getFont().getSize();
        int y = fm.getAscent();
        for(String line: lines){
            g.drawString(line, 0, y);
            y += lineHeight;
        }
    }

    private List<String> breakToLines(){
        FontMetrics fm = getFontMetrics(getFont());
        List<String> lines = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int curWidth = 0;
        for(char ch: text.toCharArray()){
            int cw = fm.charWidth(ch);
            if( curWidth > 0 && curWidth + cw > width ){
                lines.add(sb.toString());
                sb.setLength(0);
                curWidth = 0;
            }
            sb.append(ch);
            curWidth += cw;
        }
        if( sb.length() > 0 ){
            lines.add(sb.toString());
        }
        return lines;
    }

}

