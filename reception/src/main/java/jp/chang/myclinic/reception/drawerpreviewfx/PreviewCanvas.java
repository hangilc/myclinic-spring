package jp.chang.myclinic.reception.drawerpreviewfx;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jp.chang.myclinic.consts.Units;
import jp.chang.myclinic.drawer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreviewCanvas extends Canvas {

    private enum StrokeMode {
        NONE, TEXT, LINE
    }

    private static class StrokeEnv {
        Paint paint;
        double width;
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

    private static Logger logger = LoggerFactory.getLogger(PreviewCanvas.class);
    private static final double pixelPerMilliMeter;
    static {
        double inch = Units.mmToInch(1.0);
        pixelPerMilliMeter = 96 * inch;
    }
    private GraphicsContext gc;
    private Map<String, Font> fontMap = new HashMap<>();
    private Map<String, StrokeEnv> penMap = new HashMap<>();
    private StrokeMode strokeMode = StrokeMode.NONE;
    private StrokeEnv savedTextStrokeEnv;
    private StrokeEnv savedLineStrokeEnv;

    public PreviewCanvas(List<Op> ops, PaperSize paperSize) {
        this(ops, paperSize.getWidth(), paperSize.getHeight());
    }

    public PreviewCanvas(List<Op> ops, double mmWidth, double mmHeight){
        setWidth(scale(mmWidth));
        setHeight(scale(mmHeight));
        gc = getGraphicsContext2D();
        gc.setTextBaseline(VPos.TOP);
        gc.setLineWidth(0.5);
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
            }
        }
        gc.stroke();
    }

    private void doSetPen(OpSetPen op) {
        String name = op.getName();
        StrokeEnv env = penMap.get(name);
        if( env == null ){
            logger.error("Cannot find pen ({}).", name);
        } else {
            enterStrokeMode(StrokeMode.LINE);
            restoreStrokeEnv(env);
        }
    }

    private StrokeEnv getCurrentStrokeEnv(){
        StrokeEnv currentEnv = new StrokeEnv();
        currentEnv.paint = gc.getStroke();
        currentEnv.width = gc.getLineWidth();
        return currentEnv;
    }

    private void restoreStrokeEnv(StrokeEnv env){
        if( env != null ) {
            gc.setStroke(env.paint);
            gc.setLineWidth(env.width);
        }
    }

    private void enterStrokeMode(StrokeMode mode){
        if( strokeMode != mode ){
            if( mode == StrokeMode.TEXT ){
                savedLineStrokeEnv = getCurrentStrokeEnv();
                restoreStrokeEnv(savedTextStrokeEnv);
            } else {
                savedTextStrokeEnv = getCurrentStrokeEnv();
                restoreStrokeEnv(savedLineStrokeEnv);
            }
        }
    }

    private void doCreatePen(OpCreatePen op) {
        String name = op.getName();
        int r = op.getR();
        int g = op.getG();
        int b = op.getB();
        double width = scale(op.getWidth());
        StrokeEnv env = new StrokeEnv();
        env.paint = Color.color(r, g, b);
        env.width = width;
        penMap.put(name, env);
    }

    private void doSetTextColor(OpSetTextColor op) {
        int r = op.getR();
        int g = op.getG();
        int b = op.getB();
        Color color = Color.color(r, g, b);
        enterStrokeMode(StrokeMode.TEXT);
        gc.setStroke(color);
    }

    private double scale(double mm){
        return mm * pixelPerMilliMeter;
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
        enterStrokeMode(StrokeMode.TEXT);
        for(int i=0;i<n;i++){
            String s = chars.substring(i, i+1);
            double x = scale(xPosList.get(i));
            double y = scale(yPosList.get(i));
            gc.strokeText(s, x, y);
        }
    }

    private void doMoveTo(OpMoveTo op){
        double x = scale(op.getX());
        double y = scale(op.getY());
        gc.moveTo(x, y);
    }

    private void doLineTo(OpLineTo op){
        double x = scale(op.getX());
        double y = scale(op.getY());
        enterStrokeMode(StrokeMode.LINE);
        gc.lineTo(x, y);
    }

    private void doCreateFont(OpCreateFont op){
        String name = op.getName();
        String fontName = op.getFontName();
        double size = op.getSize();
        int weight = op.getWeight();
        boolean isItalic = op.isItalic();
        Font font = Font.font(
                fontName,
                FontWeight.findByWeight(weight),
                isItalic ? FontPosture.ITALIC : FontPosture.REGULAR,
                Units.mmToPoint(size));
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
}
