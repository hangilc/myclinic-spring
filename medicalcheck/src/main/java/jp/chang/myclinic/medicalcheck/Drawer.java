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
    private int nExams = 10;
    private double valueInset = 4;
    private Data data;

    List<Op> render(Data data){
        this.data = data;
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
            Box row = allocRow(h1*nExams);
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
        compiler.createFont("small-1", "MS Gothic", 3.0);
        compiler.createFont("small-2", "MS Gothic", 2.5);
        compiler.createFont("doctor-name", "MS Gothic", 4.5);
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
        String name = data.name;
        String birthday = data.birthday;
        Box[] cols = box.splitToColumns(w1, w3, 94, 143.5-w1*0.5);
        compiler.frameBottom(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("氏　名", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(name, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
        compiler.textIn("生年月日", cols[2], HAlign.Center, VAlign.Center);
        compiler.textIn(birthday, cols[3].inset(valueInset, 0), HAlign.Left, VAlign.Center);
        drawSex(cols[4]);
    }

    private void drawSex(Box box){
        String sex = data.sex;
        Box[] cols = box.splitToColumns(w1);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("性別", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(sex, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawAddress(Box box){
        String address = data.address;
        Box[] cols = box.splitToColumns(w1);
        compiler.frameBottom(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("住　所", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(address, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawShinchou(Box box){
        String height = data.height;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("身　長", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(height, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawTaijuu(Box box){
        String weight = data.weight;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("体　重", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(weight, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawShinsatsu(Box box) {
        String value = data.physicalExam;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("診　察", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(value, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawShiryoku(Box box) {
        String value = data.visualAcuity;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("視　力", cols[0], HAlign.Center, VAlign.Center);
        compiler.setFontPushing("small-1");
        compiler.textIn(value, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
        compiler.popFont();
    }

    private void drawChouryoku(Box box) {
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("聴　力", cols[0], HAlign.Center, VAlign.Center);
        compiler.setFontPushing("small-2");
        {
            Box[] valueCols = cols[1].splitToEvenColumns(2);
            Box rightCol = valueCols[0];
            Box leftCol = valueCols[1];
            drawChouryokuSub(rightCol, "右", data.hearingAbility1000Right, data.hearingAbility4000Right);
            drawChouryokuSub(leftCol, "左", data.hearingAbility1000Left, data.hearingAbility4000Left);
        }
        compiler.popFont();
    }

    private void drawChouryokuSub(Box box, String label, String value1000, String value4000){
        Box[] cols = box.splitToColumns(6);
        Box[] rows = cols[1].splitToEvenRows(2);
        compiler.textIn(label, cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn("1000Hz " + value1000, rows[0], HAlign.Left, VAlign.Center);
        compiler.textIn("4000Hz " + value4000, rows[1], HAlign.Left, VAlign.Center);
    }

    private void drawKetsuatsu(Box box) {
        String value = data.bloodPressure;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("血　圧", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(value, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawEKG(Box box) {
        String value = data.ekg;
        Box[] cols = box.splitToColumns(w1);
        compiler.box(box);
        compiler.frameInnerColumnBorders(cols);
        compiler.textIn("心電図", cols[0], HAlign.Center, VAlign.Center);
        compiler.textIn(value, cols[1].inset(valueInset, 0), HAlign.Left, VAlign.Center);
    }

    private void drawHistory(Box box){
        String value = data.history;
        Box innerBox = box.inset(1, 1);
        compiler.box(box);
        compiler.textIn("既往歴", innerBox, HAlign.Left, VAlign.Top);
        double fontSize = compiler.getCurrentFontSize();
        Box valueBox = innerBox.inset(fontSize, compiler.getCurrentFontSize() + 1, 0, 0);
        compiler.paragraph(value, valueBox, HAlign.Left, VAlign.Top, 0);
    }

    private void drawXp(Box box) {
        Box innerBox = box.inset(1, 1);
        compiler.box(box);
        compiler.textIn("胸部Ｘ線（大角）", innerBox, HAlign.Left, VAlign.Top);
        double fontSize = compiler.getCurrentFontSize();
        Box resultBox = innerBox.inset(
                fontSize,
                fontSize + 1,
                0,
                fontSize + 1
        );
        compiler.paragraph(data.chestXpResult, resultBox, HAlign.Left, VAlign.Top, 0);
        compiler.textIn(data.chestXpDate, innerBox, HAlign.Left, VAlign.Bottom);
    }

    private void drawBloodExams(Box box){
        Box[] rows = box.splitToEvenRows(nExams);
        compiler.frameInnerRowBorders(rows);
        for(int i=0;i<nExams;i++){
            Box row = rows[i];
            Box[] cols = row.splitToColumns(w2 - w1);
            Box key = cols[0];
            Box value = cols[1];
            compiler.frameInnerColumnBorders(cols);
        }
    }

    private void drawUrinalysis(Box box){
        Box[] cols = box.splitToColumns(7);
        Box[] dataRows = cols[1].splitToEvenRows(3);
        compiler.frameInnerColumnBorders(cols);
        compiler.textInVert("検　尿", cols[0], HAlign.Center, VAlign.Center);
        compiler.frameInnerRowBorders(dataRows);
    }

    private void drawBottom(Box box){
        compiler.textIn("診断の結果上記の通り相違ないことを証明する。",
                box.inset(3), HAlign.Left, VAlign.Top);
        double h1 = 75;
        double v1 = 17;
        Box sub = box.inset(h1, v1, 0, 0);
        compiler.textIn(data.clinicAddress1, sub, HAlign.Left, VAlign.Top);
        sub = sub.shiftDown(6);
        compiler.textIn(data.clinicAddress2, sub, HAlign.Left, VAlign.Top);
        sub = sub.shiftDown(6);
        compiler.textIn(data.clinicName, sub, HAlign.Left, VAlign.Top);
        sub = sub.shiftDown(8);
        compiler.setFont("doctor-name");
        Point p = new Point(sub.getLeft(), sub.getTop() + compiler.getCurrentFontSize()/2.0);
        compiler.textAt(data.doctorName, p, HAlign.Left, VAlign.Center);
        compiler.setFont("regular");
        compiler.textAt("診断医師氏名", p.shiftToLeft(3), HAlign.Right, VAlign.Center);
    }

}
