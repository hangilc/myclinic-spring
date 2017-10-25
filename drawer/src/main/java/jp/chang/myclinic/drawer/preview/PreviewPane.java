package jp.chang.myclinic.drawer.preview;

import jp.chang.myclinic.drawer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class PreviewPane extends JPanel {

    private static double displayScale = 3.78;   // dot per mm

    private double imageWidth;
    private double imageHeight;
    private double scale = displayScale;
    private List<Op> ops = Collections.emptyList();
    private Point2D currentPoint = new Point2D.Double(0, 0);
    private Color textColor;

    PreviewPane(double imageWidth, double imageHeight){
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    void setPageSize(double width, double height){
        this.imageWidth = width;
        this.imageHeight = height;
    }

    void setScale(double scale){
        this.scale = scale * displayScale;
    }

    void setOps(List<Op> ops){
        this.ops = ops;
    }

    @Override
    public Dimension getPreferredSize(){
        int width = (int)(imageWidth * scale);
        int height = (int)(imageHeight * scale);
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Map<String, Font> fontMap = new HashMap<>();
        Map<String, Color> colorMap = new HashMap<>();
        Map<String, Stroke> strokeMap = new HashMap<>();
        Graphics2D g = (Graphics2D) g1;
        ops.forEach(op -> {
            switch(op.getOpCode()){
                case MoveTo: {
                    OpMoveTo opMoveTo = (OpMoveTo)op;
                    currentPoint = new Point2D.Double(opMoveTo.getX() * scale, opMoveTo.getY() * scale);
                    break;
                }
                case LineTo: {
                    OpLineTo opLineTo = (OpLineTo)op;
                    Point2D toPoint = new Point2D.Double(opLineTo.getX() * scale, opLineTo.getY() * scale);
                    g.draw(new Line2D.Double(currentPoint, toPoint));
                    currentPoint = toPoint;
                    break;
                }
                case CreateFont: {
                    OpCreateFont opCreateFont = (OpCreateFont) op;
                    int style = 0;
                    switch (opCreateFont.getWeight()) {
                        case 0:
                        case 400:
                            style |= Font.BOLD;
                            break;
                    }
                    if (opCreateFont.isItalic()) {
                        style |= Font.ITALIC;
                    }
                    int size = (int) (opCreateFont.getSize() * scale);
                    Font font = new Font(opCreateFont.getFontName(), style, size);
                    fontMap.put(opCreateFont.getName(), font);
                    break;
                }
                case SetFont: {
                    OpSetFont opSetFont = (OpSetFont)op;
                    String name = opSetFont.getName();
                    g.setFont(fontMap.get(name));
                    break;
                }
                case DrawChars: {
                    OpDrawChars opDrawChars = (OpDrawChars)op;
                    String str = opDrawChars.getChars();
                    List<Double> xs = opDrawChars.getXs();
                    List<Double> ys = opDrawChars.getYs();
                    FontRenderContext context = g.getFontRenderContext();
                    LineMetrics lineMetrics = g.getFont().getLineMetrics(str, context);
                    double ascent = lineMetrics.getAscent();
                    Color saveColor = g.getColor();
                    g.setColor(textColor);
                    if( xs.size() == 1 ){
                        if( ys.size() == 1 ){
                            g.drawString(str, (int)(xs.get(0) * scale), (int)(ys.get(0) * scale + ascent));
                        } else {
                            int x = (int)(xs.get(0) * scale);
                            for(int i=0;i<str.length();i++){
                                int y = (int)(ys.get(i) * scale + ascent);
                                g.drawString(str.substring(i,i+1), x, y);
                            }
                        }
                    } else {
                        if( ys.size() == 1 ){
                            int y = (int)(ys.get(0) * scale + ascent);
                            for(int i=0;i<str.length();i++){
                                double x = xs.get(i);
                                g.drawString(str.substring(i, i+1), (int)(x * scale), y);
                            }
                        } else {
                            for(int i=0;i<str.length();i++){
                                int x = (int)(xs.get(i) * scale);
                                int y = (int)(ys.get(i) * scale + ascent);
                                g.drawString(str.substring(i,i+1), x, y);
                            }
                        }
                    }
//                    if( xs.size() == 1 && ys.size() == 1 ){
//                        g.drawString(str, (int)(xs.get(0) * scale), (int)(ys.get(0) * scale + ascent));
//                    } else if( xs.size() != 1 && ys.size() == 1 ){
//                        int y = (int)(ys.get(0) * scale + ascent);
//                        for(int i=0;i<str.length();i++){
//                            double x = xs.get(i);
//                            g.drawString(str.substring(i, i+1), (int)(x * scale), y);
//                        }
//                    } else if( xs.size() == 1 && ys.size() != 1 ){
//                        int x = (int)(xs.get(0) * scale);
//                        for(int i=0;i<str.length();i++){
//                            int y = (int)(ys.get(i) * scale + ascent);
//                            g.drawString(str.substring(i,i+1), x, y);
//                        }
//                    } else {
//                        for(int i=0;i<str.length();i++){
//                            int x = (int)(xs.get(i) * scale);
//                            int y = (int)(ys.get(i) * scale + ascent);
//                            g.drawString(str.substring(i,i+1), x, y);
//                        }
//                    }
                    g.setColor(saveColor);
                    break;
                }
                case SetTextColor: {
                    OpSetTextColor opSetTextColor = (OpSetTextColor)op;
                    int red = opSetTextColor.getR();
                    int green = opSetTextColor.getG();
                    int blue = opSetTextColor.getB();
                    textColor = new Color(red, green, blue);
                    break;
                }
                case CreatePen: {
                    OpCreatePen opCreatePen = (OpCreatePen)op;
                    int red = opCreatePen.getR();
                    int green = opCreatePen.getG();
                    int blue = opCreatePen.getB();
                    int width = (int)(opCreatePen.getWidth() * scale);
                    if( width <= 0 ){
                        width = 1;
                    }
                    Stroke stroke = new BasicStroke(width);
                    colorMap.put(opCreatePen.getName(), new Color(red, green, blue));
                    strokeMap.put(opCreatePen.getName(), stroke);
                    break;
                }
                case SetPen: {
                    OpSetPen opSetPen = (OpSetPen)op;
                    String name = opSetPen.getName();
                    Color color = colorMap.get(name);
                    Stroke stroke = strokeMap.get(name);
                    g.setColor(color);
                    g.setStroke(stroke);
                    break;
                }
                default: {
                    throw new RuntimeException("unknown op: " + op);
                }
            }
        });
    }

}
