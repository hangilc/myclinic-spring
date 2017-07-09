package jp.chang.myclinic.pharma.wrappedtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrappedText extends JPanel {

    private int width;
    private List<Line> lines = new ArrayList<>();
    private Line workingLine;
    private boolean mouseListenerAdded;
    private boolean inLink;

    public WrappedText(int width){
        setLayout(null);
        this.width = width;
        workingLine = createNextLine();
    }

    public WrappedText(String text, int width){
        this(width);
        appendString(text);
    }

    private interface AppendCharSeqCallback {
        void add(String text, int width, Line.VAlign align, int fontHeight, int ascent);
    }

    private void appendCharSeq(CharSequence seq, Font font, Line.VAlign valign, AppendCharSeqCallback callback){
        FontMetrics fontMetrics = getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int ascent = fontMetrics.getAscent();
        StringBuilder sb = new StringBuilder();
        int remaining = workingLine.getRemaining();
        int chunkWidth = 0;
        for(int i=0;i<seq.length();i++){
            char ch = seq.charAt(i);
            if( ch == '\n' ){
                callback.add(sb.toString(), chunkWidth, valign, fontHeight, ascent);
                newline();
                sb = new StringBuilder();
                remaining = workingLine.getRemaining();
                chunkWidth = 0;
                continue;
            }
            int cw = fontMetrics.charWidth(ch);
            if( remaining < chunkWidth + cw && !(workingLine.isEmpty() && i == 0) ){
                callback.add(sb.toString(), chunkWidth, valign, fontHeight, ascent);
                newline();
                sb = new StringBuilder();
                remaining = workingLine.getRemaining();
                chunkWidth = 0;
            }
            sb.append(ch);
            chunkWidth += cw;
        }
        if( sb.length() > 0 ){
            callback.add(sb.toString(), chunkWidth, valign, fontHeight, ascent);
        }
    }

    public void appendString(String text){
        Font font = getFont();
        appendCharSeq(text, font, Line.VAlign.BaseLine, (String chunk, int width, Line.VAlign valign, int fontHeight, int ascent) -> {
            //System.out.printf("appendString: %d %s\n", width, chunk);
            workingLine.addString(chunk, width, valign, fontHeight, ascent);
        });
        setAllSizes();
    }

    public void appendLink(String text, Runnable action){
        Font font = getFont();
        appendCharSeq(text, font, Line.VAlign.BaseLine, (String chunk, int width, Line.VAlign valign, int fontHeight, int ascent) -> {
            //System.out.printf("appendString: %s\n", chunk);
            workingLine.addLink(chunk, width, valign, fontHeight, ascent, action);
        });
        if( !mouseListenerAdded ){
            addMouseListener(makeMouseListener());
            mouseListenerAdded = true;
        }
        setAllSizes();
    }

    private void newline(){
        lines.add(workingLine);
        workingLine = createNextLine();
    }

    private Optional<Line> getLastLine(){
        if( workingLine == null ){
            return Optional.empty();
        }
        if( !workingLine.isEmpty() ){
            return Optional.of(workingLine);
        } else {
            if( lines.size() > 0 ){
                return Optional.of(lines.get(lines.size() - 1));
            } else {
                return Optional.empty();
            }
        }
    }

    private int getCurrentHeight(){
        return getLastLine().map(line -> line.getTop() + line.getHeight()).orElse(0);
    }

    private Line createNextLine(){
        return new Line(0, getCurrentHeight(), width);
    }

    public void appendComponent(JComponent component){
        if( workingLine.getRemaining() < component.getWidth() ){
            newline();
        }
        workingLine.addComponent(component, Line.VAlign.Center);
        setAllSizes();
    }

    private MouseListener makeMouseListener(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                Point p = e.getPoint();
                int x = (int)p.getX();
                int y = (int)p.getY();
                for(Line line: lines){
                    if( line.containsPoint(x, y) ){
                        line.handleClick(x, y);
                        return;
                    }
                }
                if( workingLine.containsPoint(x, y) ){
                    workingLine.handleClick(x, y);
                    return;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int x = (int)p.getX();
                int y = (int)p.getY();
                for(Line line: lines){
                    if( line.containsPoint(x, y) ){
                        boolean nowInLink = line.isInLink(x, y);
                        break;
                    }
                }
                if( workingLine.containsPoint(x, y) ){
                    boolean nowInLink = workingLine.isInLink(x, y);
                    return;
                }
            }
        };
    }

    private void setAllSizes(){
        Dimension size = new Dimension(width, getCurrentHeight());
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(Line line: lines){
            line.render(g);
        }
        if( !workingLine.isEmpty() ){
            workingLine.render(g);
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