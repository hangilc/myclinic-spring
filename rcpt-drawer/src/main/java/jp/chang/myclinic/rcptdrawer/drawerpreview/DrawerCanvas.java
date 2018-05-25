package jp.chang.myclinic.rcptdrawer.drawerpreview;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jp.chang.myclinic.drawer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerCanvas extends Canvas {

    private static Logger logger = LoggerFactory.getLogger(DrawerCanvas.class);

    private enum RenderMode {
        NONE, TEXT, PEN
    }

    private static class PenEnv {
        Paint paint;
        double width;
        int penStyle;
    }

    private static class PosList {
        private List<Double> list;

        PosList(List<Double> list){
            this.list = list;
        }

        double get(int index){
            if( index >= 0 && index < list.size() ){
                return list.get(index);
            } else {
                return list.get(list.size() - 1);
            }
        }
    }

    private static final double MilliMeterPerInch = 25.4;
    private static final double PointPerInch = 72;
    private static double mmToInch(double mm){
        return mm / MilliMeterPerInch;
    }
    private static double mmToPoint(double mm){
        return mmToInch(mm) * PointPerInch;
    }

    private static final double pixelPerMilliMeter;
    static {
        double inch = mmToInch(1.0);
        pixelPerMilliMeter = 96 * inch;
    }
    public static int mmToPixel(double mm){
        return (int)(mm * pixelPerMilliMeter);
    }
    private double contentWidth = 100;
    private double contentHeight = 100;
    private double scaleFactor = 1.0;
    private GraphicsContext gc;
    private Map<String, Font> fontMap = new HashMap<>();
    private Map<String, PenEnv> penMap = new HashMap<>();
    private RenderMode renderMode = RenderMode.NONE;
    private Color textColor;
    private PenEnv penEnv;
    private double prevX;
    private double prevY;

    public DrawerCanvas(PaperSize paperSize, double scaleFactor){
        this(paperSize.getWidth(), paperSize.getHeight(), scaleFactor);
    }

    public DrawerCanvas(double mmWidth, double mmHeight, double scaleFactor) {
        gc = getGraphicsContext2D();
        gc.setTextBaseline(VPos.TOP);
        gc.setLineWidth(0.5);
        this.contentWidth = mmWidth;
        this.contentHeight = mmHeight;
        this.scaleFactor = scaleFactor;
        updateCanvasSize();
    }

    public void setScaleFactor(double scaleFactor){
        this.scaleFactor = scaleFactor;
        updateCanvasSize();
    }

    public void setContentSize(double mmWidth, double mmHeight){
        this.contentWidth = mmWidth;
        this.contentHeight = mmHeight;
        updateCanvasSize();
    }

    private void updateCanvasSize(){
        setWidth(scale(contentWidth));
        setHeight(scale(contentHeight));
    }

    public void setOps(List<Op> ops){
        for(Op op: ops){
            switch(op.getOpCode()){
                case MoveTo: { doMoveTo((OpMoveTo)op); break; }
                case LineTo: { doLineTo((OpLineTo)op); break; }
                case CreateFont: { doCreateFont((OpCreateFont)op); break; }
                case SetFont: { doSetFont((OpSetFont)op); break; }
                case DrawChars: { doDrawChars((OpDrawChars)op); break; }
                case SetTextColor: { doSetTextColor((OpSetTextColor)op); break; }
                case CreatePen: { doCreatePen((OpCreatePen)op); break; }
                case SetPen: { doSetPen((OpSetPen)op); break; }
                case Circle: {
                    doCircle((OpCircle)op); break;
                }
            }
        }
    }

    private double scale(double mm){
        return mm * pixelPerMilliMeter * scaleFactor;
    }

    private void doSetPen(OpSetPen op) {
        String name = op.getName();
        PenEnv env = penMap.get(name);
        if( env == null ){
            logger.error("Cannot find pen ({}).", name);
        } else {
            penEnv = env;
            if( renderMode == RenderMode.PEN ){
                gc.setStroke(env.paint);
                gc.setLineWidth(env.width);
                if( env.penStyle == OpCreatePen.PS_DOT ) {
                    gc.setLineDashes(2d, 2d);
                } else {
                    gc.setLineDashes((double[])null);
                }
            }
        }
    }

    private void enterRenderMode(RenderMode mode){
        if( renderMode != mode ){
            if( mode == RenderMode.TEXT ){
                if( textColor != null ) {
                    gc.setFill(textColor);
                }
            } else {
                if( penEnv != null ){
                    gc.setStroke(penEnv.paint);
                    gc.setLineWidth(penEnv.width);
                    if( penEnv.penStyle == OpCreatePen.PS_DOT ) {
                        gc.setLineDashes(2d, 2d);
                    } else {
                        gc.setLineDashes((double[])null);
                    }
                }
            }
            renderMode = mode;
        }
    }

    private void doCreatePen(OpCreatePen op) {
        String name = op.getName();
        int r = op.getR();
        int g = op.getG();
        int b = op.getB();
        double width = scale(op.getWidth());
        PenEnv env = new PenEnv();
        env.paint = Color.color(r/255.0, g/255.0, b/255.0);
        env.width = width;
        env.penStyle = op.getPenStyle();
        penMap.put(name, env);
    }

    private void doSetTextColor(OpSetTextColor op) {
        int r = op.getR();
        int g = op.getG();
        int b = op.getB();
        textColor = Color.color(r/255.0, g/255.0, b/255.0);
        if( renderMode == RenderMode.TEXT ){
            gc.setFill(textColor);
        }
    }

    private void doDrawChars(OpDrawChars op) {
        String chars = op.getChars();
        List<Double> xs = op.getXs();
        List<Double> ys = op.getYs();
        if( xs.size() == 0 ){
            logger.error("Invalid opDrawChars xs. {}", xs);
            return;
        }
        if( ys.size() == 0 ){
            logger.error("Invalid onDrawChars ys. {}", ys);
            return;
        }
        int n = chars.length();
        PosList xPosList = new PosList(xs);
        PosList yPosList = new PosList(ys);
        enterRenderMode(RenderMode.TEXT);
        for(int i=0;i<n;i++){
            String s = chars.substring(i, i+1);
            double x = scale(xPosList.get(i));
            double y = scale(yPosList.get(i));
            gc.fillText(s, x, y);
        }
    }

    private void doMoveTo(OpMoveTo op){
        prevX = scale(op.getX());
        prevY = scale(op.getY());
        gc.moveTo(prevX, prevY);
    }

    private void doLineTo(OpLineTo op){
        double x = scale(op.getX());
        double y = scale(op.getY());
        enterRenderMode(RenderMode.PEN);
        gc.strokeLine(prevX, prevY, x, y);
        prevX = x;
        prevY = y;
    }

    private void doCreateFont(OpCreateFont op){
        String name = op.getName();
        String fontName = op.getFontName();
        double size = op.getSize();
        int weight = op.getWeight();
        boolean isItalic = op.isItalic();
        double fontSize = mmToPoint(size) * scaleFactor;
        Font font = Font.font(
                fontName,
                FontWeight.findByWeight(weight),
                isItalic ? FontPosture.ITALIC : FontPosture.REGULAR,
                fontSize);
        fontMap.put(name, font);
    }

    private void doSetFont(OpSetFont op){
        String name = op.getName();
        Font font = fontMap.get(name);
        if( font == null ) {
            logger.error("Could not find font ({}).", name);
            return;
        }
        gc.setFont(font);
    }

    private void doCircle(OpCircle op){
        double cx = scale(op.getCx());
        double cy = scale(op.getCy());
        double r = scale(op.getR());
        enterRenderMode(RenderMode.PEN);
        gc.strokeOval(cx - r, cy - r, 2*r, 2*r);
    }
}
