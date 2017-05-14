package jp.chang.myclinic.drawer;

public class Box {

    public enum HorizAnchor {
        Left, Center, Right
    }

    public enum VertAnchor {
        Top, Center, Bottom
    }

    private final double left;
    private final double top;
    private final double right;
    private final double bottom;

    public Box(double left, double top, double right, double bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public double getLeft() {
        return left;
    }

    public double getTop() {
        return top;
    }

    public double getRight() {
        return right;
    }

    public double getBottom() {
        return bottom;
    }

    public Box copy(){
        return new Box(left, top, right, bottom);
    }

    public Box innerBox(double left, double top, double right, double bottom){
        return new Box(this.left + left, this.top + top, this.left + right, this.top + bottom);
    }

    public double getWidth(){
        return right - left;
    }

    public double getHeight(){
        return bottom - top;
    }

    public double getCx(){
        return (left + right)/2.0;
    }

    public double getCy(){
        return (top + bottom)/2.0;
    }

    public Box setLeft(double left){
        return new Box(left, top, right, bottom);
    }

    public Box displaceLeftEdge(double dx){
        return new Box(left + dx, top, right, bottom);
    }

    public Box setTop(double top){
        return new Box(left, top, right, bottom);
    }

    public Box setRight(double right){
        return new Box(left, top, right, bottom);
    }

    public Box displaceRightEdge(double dx){
        return new Box(left, top, right + dx, bottom);
    }

    public Box setBottom(double bottom){
        return new Box(left, top, right, bottom);
    }

    public Box inset(double dx, double dy){
        return new Box(left + dx, top + dy, right - dx, bottom - dy);
    }

    public Box inset(double dx1, double dy1, double dx2, double dy2){
        return new Box(left + dx1, top + dy1, right - dx2, bottom - dy2);
    }

    public Box shift(double dx, double dy){
        return new Box(left + dx, top + dy, right + dx, bottom + dy);
    }

    public Box shiftUp(double dy){
        return shift(0, -dy);
    }

    public Box shiftDown(double dy){
        return shift(0, dy);
    }

    public Box shiftToRight(double dx){
        return shift(dx, 0);
    }

    public Box shiftToLeft(double dx){
        return shift(-dx, 0);
    }

    public Box shrinkWidth(double dx, HorizAnchor anchor){
        switch(anchor){
            case Left: return new Box(left, top, right - dx, bottom);
            case Center: {
                double half = dx/2.0;
                return new Box(left + half, top, right - half, bottom);
            }
            case Right: {
                return new Box(left + dx, top, right, bottom);
            }
            default: throw new RuntimeException("unknown anchor: " + anchor);
        }
    }

    public Box shrinkHeight(double dy, VertAnchor anchor){
        switch(anchor){
            case Top: return new Box(left, top, right, bottom - dy);
            case Center: {
                double half = dy/2.0;
                return new Box(left, top + half, right, bottom - half);
            }
            case Bottom: return new Box(left, top + dy, right, bottom);
            default: throw new RuntimeException("unknown anchor: " + anchor);
        }
    }

    public Box setWidth(double width, HorizAnchor anchor){
        switch(anchor){
            case Left: return new Box(left, top, left + width, bottom);
            case Center: {
                double left = getCx() - width/2.0;
                return new Box(left, top, left + width, bottom);
            }
            case Right: return new Box(left - width, top, right, bottom);
            default: throw new RuntimeException("unknown anchor: " + anchor);
        }
    }

    public Box setHeight(double height, VertAnchor anchor){
        switch(anchor){
            case Top: return new Box(left, top, right, top + height);
            case Center: {
                double top = getCy() - height/2.0;
                return new Box(left, top, right, top + height);
            }
            case Bottom: return new Box(left bottom - height, right, bottom);
            default: throw new RuntimeException("unknown anchor: " + anchor);
        }
    }

}
