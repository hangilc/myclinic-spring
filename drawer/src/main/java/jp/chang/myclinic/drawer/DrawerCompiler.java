package jp.chang.myclinic.drawer;

import java.util.*;
import java.util.stream.Collectors;

public class DrawerCompiler {

    public enum HAlign {
        Left, Center, Right
    }

    public enum VAlign {
        Top, Center, Bottom
    }

    public static class TextAtOpt {
        double extraSpace;
    }

    public enum TextInEvenColumnsJustification {
        Left, Right
    }

    private List<Op> ops = new ArrayList<>();
    private Integer currentFontSize;
    private Map<String, Point> pointDict = new HashMap<>();
    private Map<String, Box> boxDict = new HashMap<>();

    public DrawerCompiler(){

    }

    public List<Op> getOps(){
        return ops;
    }

    public void moveTo(double x, double y){
        ops.add(new OpMoveTo(x, y));
    }

    public void lineTo(double x, double y){
        ops.add(new OpLineTo(x, y));
    }

    public void line(double x1, double y1, double x2, double y2){
        moveTo(x1, y1);
        lineTo(x2, y2);
    }

    public void rectangle(double left, double top, double right, double bottom){
        moveTo(left, top);
        lineTo(right, top);
        lineTo(right, bottom);
        lineTo(left, bottom);
        lineTo(left, top);
    }

    public void box(Box box){
        rectangle(box.getLeft(), box.getTop(), box.getRight(), box.getBottom());
    }

    public void createFont(String name, String fontName, double size, int weight, boolean italic){
        ops.add(new OpCreateFont(name, fontName, size, weight, italic));
    }

    public void createFont(String name, String fontName, double size){
        createFont(name, fontName, size, 0, false);
    }

    public void setFont(String name){
        ops.add(new OpSetFont(name));
    }

    public void textAt(String text, double x, double y, HAlign halign, VAlign valign, TextAtOpt opt){
        if( text == null || text.isEmpty() ){
            return;
        }
        double extraSpace = opt == null ? 0 : opt.extraSpace;
        List<Double> mes = measureChars(text, currentFontSize);
        double totalWidth = mes.stream().reduce((a,b) -> a + b).orElse(0.0);
        double left, top;
        switch(halign){
            case Left: left = x; break;
            case Center: left = x - totalWidth/2.0; break;
            case Right: left = x - totalWidth; break;
            default: throw new RuntimeException("invalid halign: " + halign);
        }
        switch(valign){
            case Top: top = y; break;
            case Center: top = y - currentFontSize/2; break;
            case Bottom: top = y - currentFontSize; break;
            default: throw new RuntimeException("invalid valign: " + valign);
        }
        List<Double> xs = composeXs(mes, left, extraSpace);
        List<Double> ys = Collections.singletonList(top);
        ops.add(new OpDrawChars(text, xs, ys));
    }

    public void textAt(String text, double x, double y, HAlign halign, VAlign valign){
        textAt(text, x, y, halign, valign, null);
    }

    public void textAtJustified(String text, double left, double right, double y, VAlign valign){
        if( text == null || text.isEmpty() ){
            return;
        }
        System.out.println(text);
        System.out.println(currentFontSize);
        List<Double> mes = measureChars(text, currentFontSize);
        double totalWidth = mes.stream().reduce((a,b) -> a + b).orElse(0.0);
        if( text.length() < 2 ){
            textAt(text, left, y, HAlign.Left, valign);
        } else {
            double top;
            switch(valign){
                case Top: top = y; break;
                case Center: top = y - currentFontSize/2; break;
                case Bottom: top = y - currentFontSize; break;
                default: throw new RuntimeException("invalid valign: " + valign);
            }
            double extra = ((right - left) - totalWidth) / (text.length() - 1);
            List<Double> xs = composeXs(mes, left, extra);
            List<Double> ys = Collections.singletonList(top);
            ops.add(new OpDrawChars(text, xs, ys));
        }
    }

    public void textAtVert(String text, double x, double y, HAlign halign, VAlign valign){
        List<Double> mes = measureChars(text, currentFontSize);
        double totalHeight = currentFontSize * text.length();
        List<Double> xs = mes.stream().map(cw -> {
            switch(halign){
                case Left: return x;
                case Center: return x - cw / 2;
                case Right: return x - cw;
                default: throw new RuntimeException("unknown halign: " + halign);
            }
        }).collect(Collectors.toList());
        double top;
        switch(valign){
            case Top: top = y; break;
            case Center: top = y - totalHeight/2; break;
            case Bottom: top = y - totalHeight; break;
            default: throw new RuntimeException("invalid valign: " + valign);
        }
        List<Double> ys = composeYs(text.length(), top, currentFontSize, 0);
        ops.add(new OpDrawChars(text, xs, ys));
    }

    public void textAtVertJustified(String text, double x, double top, double bottom, HAlign halign){
        if( text == null || text.isEmpty() ){
            return;
        }
        List<Double> mes = measureChars(text, currentFontSize);
        if( text.length() < 2 ){
            textAt(text, x, top, halign, VAlign.Top);
            return;
        }
        List<Double> xs = mes.stream().map(cw -> {
            switch(halign){
                case Left: return x;
                case Center: return x - cw / 2;
                case Right: return x - cw;
                default: throw new RuntimeException("unknown halign: " + halign);
            }
        }).collect(Collectors.toList());
        double totalHeight = currentFontSize * text.length();
        double extra = ((bottom - top) - totalHeight) / (text.length() - 1);
        List<Double> ys = composeYs(text.length(), top, currentFontSize, extra);
        ops.add(new OpDrawChars(text, xs, ys));
    }

    public void textIn(String text, Box box, HAlign halign, VAlign valign){
        double x, y;
        switch(halign){
            case Left: x = box.getLeft(); break;
            case Center: x = box.getCx(); break;
            case Right: x = box.getRight(); break;
            default: throw new RuntimeException("invalid halign:" + halign);
        }
        switch(valign){
            case Top: y = box.getTop(); break;
            case Center: y = box.getCy(); break;
            case Bottom: y = box.getBottom(); break;
            default: throw new Error("invalid valign: " + valign);
        }
        textAt(text, x, y, halign, valign);
    }

    public void textInJustified(String text, Box box, VAlign valign){
        double y;
        switch(valign){
            case Top: y = box.getTop(); break;
            case Center: y = box.getCy(); break;
            case Bottom: y = box.getBottom(); break;
            default: throw new Error("invalid valign: " + valign);
        }
        textAtJustified(text, box.getLeft(), box.getRight(), y, valign);
    }

    public void textInVertJustified(String text, Box box, HAlign halign){
        double x;
        switch(halign){
            case Left: x = box.getLeft(); break;
            case Center: x = box.getCx(); break;
            case Right: x = box.getRight(); break;
            default: throw new RuntimeException("invalid halign:" + halign);
        }
        textAtVertJustified(text, x, box.getTop(), box.getBottom(), halign);
    }

    public void textInEvenColumns(String text, Box box, int nCols, TextInEvenColumnsJustification justifyTo){
        if( text.length() > nCols ){
            System.out.println("text is longer than columns");
        }
        int n = Math.min(text.length(), nCols);
        Box[] cols = box.splitToEvenColumns(nCols);
        if( justifyTo == TextInEvenColumnsJustification.Left ){
            for(int i=0;i<n;i++){
                textIn(text.substring(i, i+1), cols[i], HAlign.Center, VAlign.Center);
            }
        } else {
            int nPad = nCols - n;
            for(int i=0;i<n;i++){
                textIn(text.substring(i, i+1), cols[i+nPad], HAlign.Center, VAlign.Center);
            }
        }
    }

    public void setTextcolor(int red, int green, int blue){
        ops.add(new OpSetTextColor(red, green, blue));
    }

    public void createPen(String name, int red, int green, int blue, double width){
        ops.add(new OpCreatePen(name, red, green, blue, width));
    }

    public void createPen(String name, int red, int green, int blue){
        ops.add(new OpCreatePen(name, red, green, blue, 0.1));
    }

    public void setPen(String name){
        ops.add(new OpSetPen(name));
    }

    public void setPoint(String name, double x, double y){
        pointDict.put(name, new Point(x, y));
    }

    public Point getPoint(String name){
        return pointDict.get(name);
    }

    public void setBox(String name, Box box){
        boxDict.put(name, box);
    }

    public Box getBox(String name){
        return boxDict.get(name);
    }

    public void frameLeft(Box box){
        line(box.getLeft(), box.getBottom(), box.getLeft(), box.getTop());
    }

    public void frameTop(Box box){
        line(box.getLeft(), box.getTop(), box.getRight(), box.getTop());
    }

    public void frameRight(Box box){
        line(box.getRight(), box.getTop(), box.getRight(), box.getBottom());
    }

    public void frameBottom(Box box){
        line(box.getRight(), box.getBottom(), box.getLeft(), box.getBottom());
    }

    public void frameCells(Box[][] cells) {
        for(Box[] row: cells){
            for(Box cell: row){
                box(cell);
            }
        }
    }

    public void frameRightOfNthColumn(Box[][] cells, int iCol, double dx){
        double x = cells[0][iCol].getRight() + dx;
        double top = cells[0][0].getTop();
        double bottom = cells[cells.length-1][0].getBottom();
        line(x, top, x, bottom);
    }

    public void frameInnerColumnBorders(Box box, int nCol){
        double left = box.getLeft();
        double top = box.getTop();
        double bottom = box.getBottom();
        double cw = box.getWidth() / nCol;
        for(int i=1;i<nCol;i++){
            double x = left + cw * i;
            line(x, top, x, bottom);
        }
    }

    public void frameInnerColumnBorders(Box[][] cells){
        int nCol = cells[0].length;
        Box[] firstRow = cells[0];
        double top = firstRow[0].getTop();
        double bottom = cells[cells.length-1][0].getBottom();
        for(int i=1;i<firstRow.length;i++){
            Box cell = firstRow[i];
            double x = cell.getLeft();
            line(x, top, x, bottom);
        }
    }

    public List<String> breakLine(String line, double lineWidth){
        return LineBreaker.breakLine(line, currentFontSize, lineWidth);
    }

    public void multilineText(List<String> lines, Box box, HAlign halign, VAlign valign, double leading){
        if( lines == null || lines.size() == 0 ){
            return;
        }
        int nLines = lines.size();
        double y;
        switch(valign){
            case Top: y = box.getTop(); break;
            case Center: y = box.getTop() + (box.getHeight() - calcTotalHeight(nLines, currentFontSize, leading))/ 2; break;
            case Bottom: y = box.getTop() + box.getHeight() - calcTotalHeight(nLines, currentFontSize, leading); break;
            default: throw new RuntimeException("invalid valign: " + valign);
        }
        double x;
        switch(halign){
            case Left: x = box.getLeft(); break;
            case Center: x = box.getCx(); break;
            case Right: x = box.getRight(); break;
            default: throw new RuntimeException("invalid halign: " + halign);
        }
        for(String line: lines){
            textAt(line, x, y, halign, VAlign.Top);
            y += currentFontSize + leading;
        }
    }

    public double calcTotalHeight(int nLines, double fontSize, double leading){
        if( nLines == 0 ){
            return 0;
        } else {
            return nLines * fontSize + leading * (nLines - 1);
        }
    }

    private static List<Double> measureChars(String str, double fontSize){
        return str.codePoints().mapToDouble(code -> charWidth(code, fontSize)).boxed().collect(Collectors.toList());
    }

    private static double charWidth(int code, double fontSize){
        return ( code < 256 || isHankaku(code) ) ? fontSize/2 : fontSize;
    }

    private static boolean isHankaku(int code){
        return (code >= 0xff61 && code <= 0xff64) ||
                (code >= 0xff65 && code <= 0xff9f) ||
                (code >= 0xffa0 && code <= 0xffdc) ||
                (code >= 0xffe8 && code <= 0xffee);
    }

    private static List<Double> composeXs(List<Double> mes, double left, double extraSpace){
        List<Double> xs = new ArrayList<>();
        for(Double cw: mes){
            xs.add(left);
            left += cw + extraSpace;
        }
        return xs;
    }

    private static List<Double> composeYs(int nchar, double top, double fontSize, double extraSpace){
        List<Double> ys = new ArrayList<>();
        for(int i=0;i<nchar;i++){
            ys.add(top);
            top += fontSize * extraSpace;
        }
        return ys;
    }

}
