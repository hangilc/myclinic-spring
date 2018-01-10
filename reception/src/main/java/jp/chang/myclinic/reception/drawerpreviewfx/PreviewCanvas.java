package jp.chang.myclinic.reception.drawerpreviewfx;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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

    private static Logger logger = LoggerFactory.getLogger(PreviewCanvas.class);
    private static final double pixelPerMilliMeter;
    static {
        double inch = Units.mmToInch(1.0);
        pixelPerMilliMeter = 96 * inch;
    }
    private GraphicsContext gc;
    private Map<String, Font> fontMap = new HashMap<>();

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

    public PreviewCanvas(List<Op> ops){
        super(600, 600);
        gc = getGraphicsContext2D();
        gc.setTextBaseline(VPos.TOP);
        for(Op op: ops){
            switch(op.getOpCode()){
                case MoveTo: { doMoveTo((OpMoveTo)op); break; }
                case LineTo: { doLineTo((OpLineTo)op); break; }
                case CreateFont: { doCreateFont((OpCreateFont)op); break; }
                case SetFont: { doSetFont((OpSetFont)op); break; }
                case DrawChars: { doDrawChars((OpDrawChars)op); break; }
            }
        }
        gc.stroke();
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
        System.out.printf("moveto %.1f %.1f\n", x, y);
    }

    private void doLineTo(OpLineTo op){
        double x = scale(op.getX());
        double y = scale(op.getY());
        gc.lineTo(x, y);
        System.out.printf("lineto %.1f %.1f\n", x, y);
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
