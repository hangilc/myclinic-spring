package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.OpCreateFont;
import jp.chang.myclinic.drawer.OpLineTo;
import jp.chang.myclinic.drawer.OpMoveTo;

import javax.swing.*;
import java.awt.*;
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
        final boolean registerFont;
        if( fontMap == null ){
            fontMap = new HashMap<>();
            registerFont = true;
        } else {
            registerFont = false;
        }
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
                    if( registerFont ) {
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
                    }
                    break;
                }
                default: {
                    throw new RuntimeException("unknown op: " + op);
                }
            }
        });
    }
}
