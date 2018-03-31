package jp.chang.myclinic.medicalcheck;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Drawer {

    private static Logger logger = LoggerFactory.getLogger(Drawer.class);
    private DrawerCompiler compiler = new DrawerCompiler();

    private Point titlePoint = new Point(105, 30);
    private Box outerBox = new Box(19, 41, 190, 260);
    private double cy = outerBox.getTop();
    private double w1 = 22;
    private double w2 = 77;
    private double h1 = 9;

    List<Op> render(Data data){
        setupFonts();
        setupPens();
        drawTitle();
        compiler.setFont("regular");
        compiler.setPen("bold");
        compiler.box(outerBox);
        compiler.setPen("regular");
        drawShimei(allocRow(h1));
        drawAddress(allocRow(h1));
        {
            Box row = allocRow(h1*2);
            Box[] cols = row.splitToColumns(w2);
            Box[] leftRows = cols[0].splitToRows(h1);
            drawShinchou(leftRows[0]);
            drawTaijuu(leftRows[1]);
            drawHistory(cols[1]);
        }
        {
            Box row = allocRow(h1*5);
            Box[] cols = row.splitToColumns(w2);
            Box[] leftRows = cols[0].splitToEvenRows(5);
            drawShinsatsu(leftRows[0]);
            drawShiryoku(leftRows[1]);
            drawChouryoku(leftRows[3]);
            drawKetsuatsu(leftRows[4]);
            drawEKG(leftRows[2]);
            drawXp(cols[1]);
        }
        return compiler.getOps();
    }

    private void setupFonts(){
        compiler.createFont("title", "MS Gothic", 6.4, DrawerConsts.FontWeightBold, false);
        compiler.createFont("regular", "MS Gothic", 3.5);
    }

    private void setupPens(){
        compiler.createPen("bold", 0, 0, 0, 0.5);
        compiler.createPen("regular", 0, 0, 0);
    }

    private Box allocRow(double height){
        double left = outerBox.getLeft();
        double top = cy;
        double right = outerBox.getRight();
        double bottom = cy + height;
        cy = bottom;
        return new Box(left, top, right, bottom);
    }

    private void drawTitle(){
        compiler.setFont("title");
        compiler.textAt("健康診断書", titlePoint, HAlign.Center, VAlign.Center);
    }

    private void drawShimei(Box box){
        String name = "";
        String birthday = "";
        String sex = "";
        Box[] cols = box.splitToColumns(w1, w2, 94, 143.5);
        compiler.frameBottom(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("氏　名", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(name, cols[1].inset(2, 0), HAlign.Left, VAlign.Center);
        compiler.textIn("生年月日", cols[2], HAlign.Center, VAlign.Center);
        compiler.textIn(birthday, cols[3].inset(2, 0), HAlign.Left, VAlign.Center);
        compiler.textIn(sex, cols[4], HAlign.Center, VAlign.Center);
    }

    private void drawAddress(Box box){
        String address = "";
        Box[] cols = box.splitToColumns(w1);
        compiler.frameBottom(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("住　所", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(address, cols[1].inset(2, 0), HAlign.Left, VAlign.Center);
    }

    private void drawShinchou(Box box){
        compiler.box(box);
    }

    private void drawTaijuu(Box box){
        compiler.box(box);
    }

    private void drawHistory(Box box){
        compiler.box(box);
    }

    private void drawXp(Box box) {
        compiler.box(box);

    }

    private void drawKetsuatsu(Box box) {
        compiler.box(box);

    }

    private void drawChouryoku(Box box) {
        compiler.box(box);

    }

    private void drawShiryoku(Box box) {
        compiler.box(box);

    }

    private void drawShinsatsu(Box box) {
        compiler.box(box);

    }

    private void drawEKG(Box box) {
        compiler.box(box);

    }

}
