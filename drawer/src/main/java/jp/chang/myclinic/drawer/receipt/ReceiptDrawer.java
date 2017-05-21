package jp.chang.myclinic.drawer.receipt;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.Box.HorizAnchor;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;

import java.util.List;

/**
 * Created by hangil on 2017/05/20.
 */
public class ReceiptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private ReceiptDrawerData data;

    public ReceiptDrawer(ReceiptDrawerData data){
        this.data = data;
        setupFonts();
        compiler.createPen("regular", 0, 0, 0, 0.1);
        compiler.setPen("regular");
        Box frameBox = new Box(0, 0, 148, 105);
        Box titleBox = frameBox.shiftDown(4).setWidth(28, HorizAnchor.Center)
                .setHeight(6, VertAnchor.Top);
        Box row1 = frameBox.innerBox(13, 14, 73, 23);
        Box row2 = frameBox.shiftDown(row1.getBottom()+3)
                .setHeight(4, VertAnchor.Top).setLeft(13).setWidth(60, HorizAnchor.Left);
        Box row3 = frameBox.setTop(row2.getBottom()+3)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box row4 = frameBox.setTop(row3.getBottom()+3)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box row5 = frameBox.setTop(row4.getBottom()+1)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box hokengaiBox = frameBox.setTop(row5.getBottom()+3)
                .setHeight(25, VertAnchor.Top).setLeft(13).setWidth(48, HorizAnchor.Left);
        Box instituteBox = hokengaiBox.flipRight().shiftToRight(11)
                .setHeight(25, VertAnchor.Top).setWidth(30, HorizAnchor.Left);
        Box ryoushuuBox = instituteBox.flipRight().shiftToRight(7)
                .setHeight(29, VertAnchor.Top).setWidth(24, HorizAnchor.Left);
        compiler.box(titleBox);
        compiler.box(row1);
        compiler.box(row2);
        compiler.box(row3);
        compiler.box(row4);
        compiler.box(row5);
        compiler.box(hokengaiBox);
        compiler.box(instituteBox);
        compiler.box(ryoushuuBox);
        mainTitle(titleBox);
        renderRow1(row1, data.getPatientName(), data.getCharge());
        renderRow2(row2, data.getVisitDate(), data.getIssueDate());
        renderRow3(row3, data.getPatientId(), data.getHoken(), data.getFutanWari());
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    private void setupFonts(){
        compiler.createFont("mincho-6", "MS Mincho", 6);
        compiler.createFont("mincho-4", "MS Mincho", 4);
        compiler.createFont("gothic-5", "MS Gothic", 5);
        compiler.createFont("gothic-4", "MS Gothic", 4);
        compiler.createFont("gothic-2.6", "MS Gothic", 2.6);
    }

    private void mainTitle(Box box){
        compiler.setFont("mincho-6");
        compiler.textInJustified("領収証", box, VAlign.Top);
    }

    private void renderRow1(Box box, String name, String charge){
        compiler.setFont("mincho-6");
        compiler.frameBottom(box);
        compiler.textIn("様", box, HAlign.Right, VAlign.Bottom);
        Box nameBox = box.shrinkWidth(8, HorizAnchor.Left);
        compiler.textIn(name, nameBox, HAlign.Center, VAlign.Bottom);
        Box chargeBox = box.flipRight().shiftToRight(8).setWidth(52, HorizAnchor.Left);
        compiler.textIn("領収金額", chargeBox, HAlign.Left, VAlign.Bottom);
        compiler.textIn("円", chargeBox, HAlign.Right, VAlign.Bottom);
        compiler.frameBottom(chargeBox);
        Box kingakuBox = chargeBox.displaceLeftEdge(24).displaceRightEdge(-6.9);
        compiler.setFont("gothic-5");
        compiler.textIn(charge, kingakuBox, HAlign.Right, VAlign.Bottom);
    }

    private void renderRow2(Box box, String visitDate, String issueDate){
        compiler.setFont("mincho-4");
        compiler.textIn("診察日", box, HAlign.Left, VAlign.Center);
        Box dateBox = box.shrinkWidth(16, HorizAnchor.Right);
        compiler.textIn(visitDate, dateBox, HAlign.Left, VAlign.Center);
        Box issueBox = box.flipRight().shiftToRight(6);
        compiler.textIn("発効日", issueBox, HAlign.Left, VAlign.Center);
        compiler.textIn(issueDate, issueBox.displaceLeftEdge(16), HAlign.Left, VAlign.Center);
    }

    private void renderRow3(Box box, String patientId, String hoken, String futanWari){
        Box[][] cells = box.splitToEvenCells(2, 3);
        compiler.frameCells(cells);
        compiler.setFont("mincho-4");
        compiler.textIn("患者番号", cells[0][0], HAlign.Center, VAlign.Center);
        compiler.textIn("保険種別", cells[0][1], HAlign.Center, VAlign.Center);
        compiler.textIn("負担割合", cells[0][2], HAlign.Center, VAlign.Center);
        compiler.textIn( patientId, cells[1][0], HAlign.Center, VAlign.Center);
        compiler.textIn( hoken, cells[1][1], HAlign.Center, VAlign.Center);
        compiler.textIn(futanWari, cells[1][2], HAlign.Center, VAlign.Center);

    }
}
