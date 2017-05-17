package jp.chang.myclinic.drawer;

/**
 * Created by hangil on 2017/05/14.
 */
public class OpCreatePen extends Op {

    private final String name;
    private final int r;
    private final int g;
    private final int b;
    private final double width;

    public OpCreatePen(String name, int r, int g, int b, double width){
        super(OpCode.CreatePen);
        this.name = name;
        this.r = r;
        this.g = g;
        this.b = b;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public double getWidth() {
        return width;
    }
}
