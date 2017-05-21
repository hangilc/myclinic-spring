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
        Box titleBox = frameBox.copy().shiftDown(4).setWidth(28, HorizAnchor.Center)
                .setHeight(6, VertAnchor.Top);
        Box row1 = frameBox.innerBox(13, 14, 73, 23);
        Box row2 = frameBox.copy().shiftDown(row1.getBottom()+3)
                .setHeight(4, VertAnchor.Top).setLeft(13).setWidth(60, HorizAnchor.Left);
        Box row3 = frameBox.copy().setTop(row2.getBottom()+3)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box row4 = frameBox.copy().setTop(row3.getBottom()+3)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box row5 = frameBox.copy().setTop(row4.getBottom()+1)
                .setHeight(10, VertAnchor.Top).setLeft(13).setWidth(120, HorizAnchor.Left);
        Box hokengaiBox = frameBox.copy().setTop(row5.getBottom()+3)
                .setHeight(25, VertAnchor.Top).setLeft(13).setWidth(48, HorizAnchor.Left);
        Box instituteBox = hokengaiBox.copy().flipRight().shiftToRight(11)
                .setHeight(25, VertAnchor.Top).setWidth(30, HorizAnchor.Left);
        Box ryoushuuBox = instituteBox.copy().flipRight().shiftToRight(7)
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
//        name = name || "";
//        charge = charge || "";
//        if( typeof charge === "number" ){
//            charge = util.formatNumber(charge);
//        }
//        var c = this.compiler;
//        c.setFont("mincho-6");
//        c.frameBottom(box);
//        c.textIn("様", box, "right", "bottom");
//        var nameBox = box.clone().shrinkWidth(8, "left");
//        c.textIn(name, nameBox, "center", "bottom");
//        var chargeBox = box.flipRight().shiftToRight(8).setWidth(52, "left");
//        c.textIn("領収金額", chargeBox, "left", "bottom");
//        c.textIn("円", chargeBox, "right", "bottom");
//        c.frameBottom(chargeBox);
//        var kingakuBox = chargeBox.clone().displaceLeftEdge(24).displaceRightEdge(-6.9);
//        c.setFont("gothic-5");
//        c.textIn(charge, kingakuBox, "right", "bottom");

    }
}
