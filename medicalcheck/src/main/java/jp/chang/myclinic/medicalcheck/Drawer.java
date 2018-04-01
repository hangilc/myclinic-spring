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
    private double w2 = 59;
    private double w3 = 77;
    private double w4 = 113;
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
            Box[] cols = row.splitToColumns(w3);
            Box[] leftRows = cols[0].splitToRows(h1);
            drawShinchou(leftRows[0]);
            drawTaijuu(leftRows[1]);
            drawHistory(cols[1]);
        }
        {
            Box row = allocRow(h1*5);
            Box[] cols = row.splitToColumns(w3);
            Box[] leftRows = cols[0].splitToEvenRows(5);
            drawShinsatsu(leftRows[0]);
            drawShiryoku(leftRows[1]);
            drawChouryoku(leftRows[2]);
            drawKetsuatsu(leftRows[3]);
            drawEKG(leftRows[4]);
            drawXp(cols[1]);
        }
        {
            Box row = allocRow(h1*9);
            Box[] cols = row.splitToColumns(w1, w4);
            compiler.box(row);
            compiler.frameInnerColumnBorders(cols);
            compiler.textIn("血液検査", cols[0], HAlign.Center, VAlign.Center);
            drawBloodExams(cols[1]);
            {
                Box[] rightRows = cols[2].splitToRows(h1*3);
                compiler.frameInnerRowBorders(rightRows);
                drawUrinalysis(rightRows[0]);
                compiler.textIn("その他特記事項", rightRows[1].inset(1, 1), HAlign.Left, VAlign.Top);
            }
        }
        {
            Box bottom = outerBox.setTop(cy);
            drawBottom(bottom);
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
        Box[] cols = box.splitToColumns(w1, w3, 94, 143.5);
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
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("身　長", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawTaijuu(Box box){
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("体　重", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawShinsatsu(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("診　察", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawShiryoku(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("視　力", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawChouryoku(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("聴　力", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawKetsuatsu(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("血　圧", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawEKG(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("心電図", cols[0], HAlign.Center, VAlign.Center);
    }

    private void drawHistory(Box box){
        compiler.box(box);
        compiler.textIn("既往歴", box.inset(1, 1), HAlign.Left, VAlign.Top);
    }

    private void drawXp(Box box) {
        compiler.box(box);
        compiler.textIn("胸部Ｘ線（大角）", box.inset(1, 1), HAlign.Left, VAlign.Top);
    }

    private void drawBloodExams(Box box){
        Box[] rows = box.splitToEvenRows(9);
        compiler.frameInnerRowBorders(rows);
        for(int i=0;i<9;i++){
            Box row = rows[i];
            Box[] cols = row.splitToColumns(w2 - w1);
            Box key = cols[0];
            Box value = cols[1];
            compiler.frameInnerColumnBorders(cols);
        }
    }

    private void drawUrinalysis(Box box){
        Box[] cols = box.splitToColumns(7);
        compiler.frameInnerColumnBorders(cols);
        compiler.textInVert("検　尿", cols[0], HAlign.Center, VAlign.Center);
        Box[] dataRows = cols[1].splitToEvenRows(3);
    }

    private void drawBottom(Box box){
        compiler.textIn("診断の結果上記の通り相違ないことを証明する。", box.inset(3), HAlign.Left, VAlign.Top);
    }

}
