package jp.chang.myclinic.pharma.wrappedtext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WrappedText extends JPanel {

    private int width;
    private List<Line> lines = new ArrayList<>();
    private Line workingLine;
    private boolean mouseListenerAdded;
    private boolean inLink;
    private Cursor defaultCursor;
    private Cursor linkCursor;

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
            workingLine.addString(chunk, width, valign, fontHeight, ascent);
        });
        setAllSizes();
    }

    public void appendLink(String text, Runnable action){
        Font font = getFont();
        appendCharSeq(text, font, Line.VAlign.BaseLine, (String chunk, int width, Line.VAlign valign, int fontHeight, int ascent) -> {
            workingLine.addLink(chunk, width, valign, fontHeight, ascent, action);
        });
        if( !mouseListenerAdded ){
            addMouseListener(makeMouseListener());
            addMouseMotionListener(makeMouseMotionListener());
            defaultCursor = getCursor();
            linkCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
            mouseListenerAdded = true;
        }
        setAllSizes();
    }

    public void appendComponent(JComponent component){
        Dimension dim = component.getPreferredSize();
        component.setMinimumSize(dim);
        component.setMaximumSize(dim);
        if( workingLine.getRemaining() < dim.getWidth() ){
            newline();
        }
        workingLine.addComponent(component, Line.VAlign.Center);
        add(component);
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
                }
            }
        };
    }

    private MouseMotionListener makeMouseMotionListener(){
        return new MouseAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                Point p = e.getPoint();
                int x = (int)p.getX();
                int y = (int)p.getY();
                boolean nowInLink = false;
                for(Line line: lines){
                    if( line.containsPoint(x, y) ){
                        nowInLink = line.isInLink(x, y);
                        break;
                    }
                }
                if( workingLine.containsPoint(x, y) ){
                    nowInLink = workingLine.isInLink(x, y);
                }
                if( inLink != nowInLink ){
                    if( nowInLink ){
                        setCursor(linkCursor);
                    } else {
                        setCursor(defaultCursor);
                    }
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
