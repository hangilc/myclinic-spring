package jp.chang.myclinic;

import jp.chang.myclinic.drawer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hangil on 2017/05/17.
 */
public class DrawerPreviewPane extends JPanel {

    private List<Op> ops;
    private double imageWidth;
    private double imageHeight;
    private Point2D currentPoint;
    private Map<String, Font> fontMap;
    private Color textColor;
    private Map<String, Color> colorMap;
    private Map<String, Stroke> strokeMap;

    public DrawerPreviewPane(List<Op> ops, double imageWidth, double imageHeight){
        this.ops = ops;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public void paintComponent(Graphics g1){
        super.paintComponent(g1);
        double xScale = getWidth() / imageWidth;
        double yScale = getHeight() / imageHeight;
        double scale = Math.min(xScale, yScale);
        fontMap = new HashMap<>();
        colorMap = new HashMap<>();
        strokeMap = new HashMap<>();
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
                    int size = (int) (opCreateFont.getSize() * scale / 25.4 * 72);
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
                    if( xs.size() == 1 && ys.size() == 1 ){
                        g.drawString(str, (int)(xs.get(0) * scale), (int)(ys.get(0) * scale + ascent));
                    } else if( xs.size() != 1 && ys.size() == 1 ){
                        int y = (int)(ys.get(0) * scale + ascent);
                        for(int i=0;i<str.length();i++){
                            double x = xs.get(i);
                            g.drawString(str.substring(i, i+1), (int)(x * scale), y);
                        }
                    } else if( xs.size() == 1 && ys.size() != 1 ){
                        int x = (int)(xs.get(0) * scale);
                        for(int i=0;i<str.length();i++){
                            int y = (int)(ys.get(i) * scale + ascent);
                            g.drawString(str.substring(i,i+1), x, y);
                        }
                    } else {
                        for(int i=0;i<str.length();i++){
                            int x = (int)(xs.get(i) * scale);
                            int y = (int)(ys.get(i) * scale + ascent);
                            g.drawString(str.substring(i,i+1), x, y);
                        }
                    }
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
