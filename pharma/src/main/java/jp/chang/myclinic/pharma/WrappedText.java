package jp.chang.myclinic.pharma;

import jp.chang.myclinic.pharma.wrappedtext.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class WrappedText extends JPanel {

    private int width;
//    private int ascent;
//    private int fontSize;
//    private List<StringItem> stringItems = new ArrayList<>();
//    private List<LinkItem> linkItems;
//    private int posX;
//    private int posY;
//    private int botY;
    private List<Line> lines = new ArrayList<>();
    private Line currentLine;

    public WrappedText(int width){
        setLayout(null);
        this.width = width;
//        Font font = getFont();
//        this.ascent = getFontMetrics(font).getAscent();
//        this.fontSize = font.getSize();
//        this.botY = this.fontSize;
    }

    public WrappedText(String text, int width){
        this(width);
        appendString(text);
    }

//    private List<StringChunk> breakToLines(String text){
//        List<StringChunk> result = new ArrayList<>();
//        FontMetrics fm = getFontMetrics(getFont());
//        StringBuilder sb = new StringBuilder();
//        int x = posX;
//        for(char ch: text.toCharArray()){
//            if( ch == '\n' ){
//                result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
//                sb.setLength(0);
//                newline();
//                x = 0;
//                continue;
//            }
//            int cw = fm.charWidth(ch);
//            if( x > 0 && x + cw > width ){
//                result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
//                sb.setLength(0);
//                newline();
//                x = 0;
//            }
//            sb.append(ch);
//            x += cw;
//        }
//        if( sb.length() > 0 ){
//            result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
//            posX = x;
//        }
//        return result;
//    }

//    private StringItem stringChunkToStringItem(StringChunk chunk){
//        Rectangle rect = chunk.rect;
//        return new StringItem((int)rect.getX(), (int)rect.getY() + ascent, chunk.text);
//    }

    public void appendString(String text){
        if( text.isEmpty() ){
            return;
        }
        Font font = getFont();
        FontMetrics fontMetrics = getFontMetrics(font);
        Line.VAlign valign = Line.VAlign.BaseLine;
        ensureCurrentLine();
        for(int i=0;i<text.length();i++){
            char ch = text.charAt(i);
            if( ch == '\n' || !currentLine.addChar(ch, valign, fontMetrics) ){
                newline();
                currentLine.addChar(ch, valign, fontMetrics);
            }
        }
        setAllSizes();
    }

    private void ensureCurrentLine(){
        if( currentLine == null ){
            currentLine = createNextLine();
            lines.add(currentLine);
        }
    }

    private void newline(){
        currentLine = createNextLine();
        lines.add(currentLine);
    }

    private Line createNextLine(){
        return new Line(0, getCurrentHeight(), width);
    }

    public void appendComponent(JComponent component){
        ensureCurrentLine();
        Line.VAlign valign = Line.VAlign.Center;
        if( !currentLine.addComponent(component, valign) ){
            newline();
            currentLine.addComponent(component, valign);
        }
        add(component);
        setAllSizes();
    }

    public void appendLink(String text, Runnable action){
//        List<LinkItem> items = breakToLines(text).stream().map(chunk -> {
//            StringItem stringItem = stringChunkToStringItem(chunk);
//            return new LinkItem(stringItem, chunk.rect, action);
//        }).collect(Collectors.toList());
//        if( items.size() > 0 ){
//            if( linkItems == null ){
//                linkItems = new ArrayList<>();
//                addMouseListener(makeMouseListener());
//            }
//            linkItems.addAll(items);
//        }
    }

    private MouseListener makeMouseListener(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
//                super.mouseClicked(e);
//                Point p = e.getPoint();
//                for(LinkItem linkItem: linkItems){
//                    if( linkItem.contains(p) ){
//                        linkItem.doAction();
//                        return;
//                    }
//                }
            }
        };
    }

    private void setAllSizes(){
        Dimension size = new Dimension(width, getCurrentHeight());
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    private int getCurrentHeight(){
        if( lines.size() > 0 ){
            Line lastLine = lines.get(lines.size() - 1);
            return lastLine.getTop() + lastLine.getHeight();
        } else {
            return 0;
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Line line: lines){
            line.render(g);
        }
        paintComponents(g);
    }
}

/*
class WrappedTextOrig extends JPanel {

    private int width;
    private int ascent;
    private int fontSize;
    private List<StringItem> stringItems = new ArrayList<>();
    private List<LinkItem> linkItems;
    private int posX;
    private int posY;
    private int botY;

    public WrappedTextOrig(int width){
        setLayout(null);
        this.width = width;
        Font font = getFont();
        this.ascent = getFontMetrics(font).getAscent();
        this.fontSize = font.getSize();
        this.botY = this.fontSize;
    }

    public WrappedTextOrig(String text, int width){
        this(width);
        appendString(text);
    }

    private List<StringChunk> breakToLines(String text){
        List<StringChunk> result = new ArrayList<>();
        FontMetrics fm = getFontMetrics(getFont());
        StringBuilder sb = new StringBuilder();
        int x = posX;
        for(char ch: text.toCharArray()){
            if( ch == '\n' ){
                result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
                sb.setLength(0);
                newline();
                x = 0;
                continue;
            }
            int cw = fm.charWidth(ch);
            if( x > 0 && x + cw > width ){
                result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
                sb.setLength(0);
                newline();
                x = 0;
            }
            sb.append(ch);
            x += cw;
        }
        if( sb.length() > 0 ){
            result.add(new StringChunk(sb.toString(), new Rectangle(posX, posY, x - posX, fontSize)));
            posX = x;
        }
        return result;
    }

    private StringItem stringChunkToStringItem(StringChunk chunk){
        Rectangle rect = chunk.rect;
        return new StringItem((int)rect.getX(), (int)rect.getY() + ascent, chunk.text);
    }

    public void appendString(String text){
        stringItems.addAll(breakToLines(text).stream().map(this::stringChunkToStringItem).collect(Collectors.toList()));
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
        List<LinkItem> items = breakToLines(text).stream().map(chunk -> {
            StringItem stringItem = stringChunkToStringItem(chunk);
            return new LinkItem(stringItem, chunk.rect, action);
        }).collect(Collectors.toList());
        if( items.size() > 0 ){
            if( linkItems == null ){
                linkItems = new ArrayList<>();
                addMouseListener(makeMouseListener());
            }
            linkItems.addAll(items);
        }
    }

    private MouseListener makeMouseListener(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Point p = e.getPoint();
                for(LinkItem linkItem: linkItems){
                    if( linkItem.contains(p) ){
                        linkItem.doAction();
                        return;
                    }
                }
            }
        };
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
        for(StringItem stringItem: stringItems){
            stringItem.render(g);
        }
        paintComponents(g);
        if( linkItems != null ){
            Color colorSave = g.getColor();
            g.setColor(Color.BLUE);
            for(LinkItem linkItem: linkItems){
                linkItem.render(g);
            }
            g.setColor(colorSave);
        }
    }

}
*/