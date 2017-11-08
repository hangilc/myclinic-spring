package jp.chang.myclinic.practice.shohousen;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.Box.HorizAnchor;

import java.util.List;

public class ShohousenDrawer {

    private ShohousenData data;
    private DrawerCompiler compiler = new DrawerCompiler();
    private Box wrap;

    public ShohousenDrawer(ShohousenData data){
        this.data = data;
        setupFonts();
        compiler.setTextColor(0, 255, 0);
        compiler.createPen("default-pen", 0, 255, 0, 0.1);
        compiler.setPen("default-pen");
        wrap = Box.of(PaperSize.A5);
        drawTitle();
        Box r = wrap.shiftDown(13).setHeight(10.5, VertAnchor.Top);
        Box[] rr1 = r.splitToColumns(62);
        drawKouhi(rr1[0].shrinkWidth(2, HorizAnchor.Left));
        drawHoken(rr1[1]);
        Box box2 = wrap.setTop(r.getBottom()+2).setHeight(154.5, VertAnchor.Top);
        Box[] rr2 = box2.splitToRows(18, 24.5, 109, 143, 149.5);
        Box pat = rr2[0];
        Box issue = rr2[1];
        Box drugs = rr2[2];
        Box memo = rr2[3];
        Box chouzai1 = rr2[4];
        Box chouzai2 = rr2[5];
        Box[] rr3 = pat.splitToColumns(55);
        Box patient = rr3[0];
        Box clinic = rr3[1].shrinkWidth(1, HorizAnchor.Right);
        drawPatient(patient);
        drawClinic(clinic);
//        drawIssue(issue);
//        drawDrugs(drugs);
//        drawMemo(memo);
//        drawChouzai1(chouzai1);
//        drawChouzai2(chouzai2);
//        Box pharma = wrap.setTop(box2.getBottom() + 1).setHeight(24.5, VertAnchor.Top);
//        drawPharmacy(pharma);
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    private void frameDate(Box[] cols){
        double offset = 1;
        compiler.textIn("年", cols[0].flipRight().shiftToRight(offset), HAlign.Left, VAlign.Center);
        compiler.textIn("月", cols[1].flipRight().shiftToRight(offset), HAlign.Left, VAlign.Center);
        compiler.textIn("日", cols[2].flipRight().shiftToRight(offset), HAlign.Left, VAlign.Center);
    }

    private void setupFonts(){
        compiler.createFont("mincho-5", "MS Mincho", 5);
        compiler.createFont("mincho-4.5", "MS Mincho", 4.5);
        compiler.createFont("mincho-4", "MS Mincho", 4);
        compiler.createFont("mincho-3.5", "MS Mincho", 3.5);
        compiler.createFont("mincho-3", "MS Mincho", 3);
        compiler.createFont("mincho-2.5", "MS Mincho", 2.5);
        compiler.createFont("mincho-2", "MS Mincho", 2);
        compiler.createFont("mincho-1.8", "MS Mincho", 1.8);
        compiler.createFont("mincho-1.5", "MS Mincho", 1.5);
        compiler.createFont("mincho-1.4", "MS Mincho", 1.4);
        compiler.createFont("gothic-4.5", "MS Gothic", 4.5);
        compiler.createFont("gothic-4", "MS Gothic", 4);
        compiler.createFont("gothic-3", "MS Gothic", 3);
        compiler.createFont("gothic-2.5", "MS Gothic", 2.5);
    }

    private void drawTitle(){
        Box box1 = wrap.shiftDown(1).setLeft(51).setRight(93);
        compiler.setFont("mincho-5");
        compiler.textAtJustified("処方せん", box1.getLeft(), box1.getRight(), box1.getTop(), VAlign.Top);
        Box box2 = box1.shiftDown(6);
        compiler.setFont("mincho-2.5");
        compiler.textIn("(この処方せんは、どの保険薬局でも有効です。)", box2, HAlign.Center, VAlign.Top);
    }

    private void drawKouhi(Box b){
        Box[] rr = b.splitToEvenRows(2);
        Box row1 = rr[0];
        compiler.box(row1);
        Box[] cc1 = row1.splitToColumns(14.3);
        compiler.frameRight(cc1[0]);
        compiler.setFont("mincho-2");
        compiler.textAtJustified("公費負担者番号", cc1[0].getLeft()+0.5, cc1[0].getRight()-0.5, cc1[0].getCy(), VAlign.Center);
        //compiler.setBox("futanshaBangou", cc[1]);
        compiler.frameInnerColumnBorders(cc1[1], 8);
        Box row2 = rr[1].shrinkWidth(cc1[1].getWidth()/8, HorizAnchor.Left);
        compiler.box(row2);
        Box[] cc2 = row2.splitToColumns(14.3);
        compiler.frameRight(cc2[0]);
        compiler.textAtJustified("公費負担医療", cc2[0].getLeft()+0.5, cc2[0].getRight()-0.5, cc2[0].getTop()+cc2[0].getHeight()/4, VAlign.Center);
        compiler.textAtJustified("の受給者番号", cc2[0].getLeft()+0.5, cc2[0].getRight()-0.5, cc2[0].getTop()+cc2[0].getHeight()/4*3, VAlign.Center);
        //compiler.setBox("jukyuushaBangou", cc2[1]);
        compiler.frameInnerColumnBorders(cc2[1], 7);
    }

    private void drawHoken(Box r){
        DrawerCompiler c = compiler;
        Box[] rr = r.splitToEvenRows(2);
        Box upper = rr[0];
        Box lower = rr[1];
        upper = upper.setWidth(58, HorizAnchor.Left);
        c.box(upper);
        rr = upper.splitToColumns(13);
        Box left = rr[0];
        Box right = rr[1];
        c.frameRight(left);
        c.setFont("mincho-2");
        c.textAtJustified("保険者番号", left.getLeft()+0.5, left.getRight()-0.5, left.getCy(), VAlign.Center);
        c.setBox("hokenshaBangou", right);
        c.frameInnerColumnBorders(right, 8);
        c.box(lower);
        System.out.println(lower);
        System.out.println(PaperSize.A5);
        rr = lower.splitToColumns(13);
        left = rr[0];
        right = rr[1];
        c.setBox("hihokensha", right);
        c.frameRight(left);
        c.setFont("mincho-1.4");
        c.textAtJustified("被保険者証・被保険", left.getLeft()+0.5, left.getRight()-0.5, left.getTop()+left.getHeight()/4, VAlign.Center);
        c.textAtJustified("者手帳の記号・番号", left.getLeft()+0.5, left.getRight()-0.5, left.getTop()+left.getHeight()/4*3, VAlign.Center);
    }

    private void drawPatient(Box r) {
        DrawerCompiler c = this.compiler;
        c.box(r);
        Box p = r.setWidth(4, HorizAnchor.Left);
        c.frameRight(p);
        c.setFont("mincho-2.5");
        c.textAtVertJustified("患者", p.getCx(), p.getTop()+4, p.getBottom()-4, HAlign.Center);
        Box[] rr = p.setLeft(p.getRight()).setRight(r.getRight()).splitToRows(9.5, 13.8);
        c.frameBottom(rr[0]);
        c.frameBottom(rr[1]);
        Box upper = rr[0];
        Box middle = rr[1];
        Box lower = rr[2];
        rr = upper.splitToColumns(10.5);
        p = rr[0];
        c.setBox("patientName", rr[1]);
        c.frameRight(p);
        c.setFont("mincho-2.5");
        c.textAtJustified("氏名", p.getLeft()+2, p.getRight()-2, p.getCy(), VAlign.Center);
        rr = middle.splitToColumns(10.5, 39);
        p = rr[0];
        c.frameRight(p);
        c.setFont("mincho-2");
        c.textAtJustified("生年月日", p.getLeft()+0.5, p.getRight()-0.5, p.getCy(), VAlign.Center);
        p = rr[1];
        c.frameRight(p);
        Box[] dd = p.splitToColumns(9, 17, 25);
        c.setBox("birthdayYear", dd[0]);
        c.setBox("birthdayMonth", dd[1]);
        c.setBox("birthdayDay", dd[2]);
        frameDate(dd);
        p = rr[2];
        dd = p.splitToEvenColumns(3);
        c.setBox("sexMale", dd[0]);
        c.setBox("sexFemale", dd[2]);
        c.textIn("男", dd[0], HAlign.Center, VAlign.Center);
        c.textIn("・", dd[1], HAlign.Center, VAlign.Center);
        c.textIn("女", dd[2], HAlign.Center, VAlign.Center);
        rr = lower.splitToColumns(10.5, 24, 37.3);
        c.setBox("patientHihokensha", rr[1]);
        c.setBox("patientHifuyousha", rr[2]);
        Box futanBox = rr[3].shrinkWidth(4, HorizAnchor.Left);
        c.setBox("patientFutan", futanBox);
        c.frameInnerColumnBorders(rr);
        c.setFont("mincho-2.5");
        c.textAtJustified("区分", rr[0].getLeft()+2, rr[0].getRight()-2, rr[0].getCy(), VAlign.Center);
        c.textInJustified("被保険者", rr[1].inset(1.5, 0), VAlign.Center);
        c.textInJustified("被扶養者", rr[2].inset(1.5, 0), VAlign.Center);
        c.textIn("割", futanBox.shiftToRight(3), HAlign.Right, VAlign.Center);
    }

    private void drawClinic(Box box){
        DrawerCompiler c = this.compiler;
        Box[] rr = box.splitToRows(9.5, 13.8);
        Box upper = rr[0];
        Box middle = rr[1];
        Box lower = rr[2];
        rr = upper.splitToColumns(11);
        Box p = rr[0];
        c.setBox("clinicInfo", rr[1]);
        p.shrinkHeight(1.5, VertAnchor.Bottom);
        p.shrinkHeight(0.5, VertAnchor.Bottom);
        Box[] pp = p.splitToEvenRows(3);
        c.setFont("mincho-1.5");
        c.textInJustified("保険医療機関", pp[0], VAlign.Top);
        c.setFont("mincho-1.8");
        c.textInJustified("の所在地", pp[1], VAlign.Center);
        c.textInJustified("及び名称", pp[2], VAlign.Bottom);
        rr = middle.splitToColumns(11);
        c.setBox("clinicPhone", rr[1]);
        c.textInJustified("電話番号", rr[0], VAlign.Center);
        rr = lower.splitToColumns(11);
        c.setBox("clinicDoctor", rr[1]);
        c.textInJustified("保険医氏名", rr[0], VAlign.Center);
        c.setBox("clinicHanko", new Box(
                box.getLeft() + 53.5+7, box.getBottom() - 5.5, box.getLeft() + 56.5+7, box.getBottom() - 2.5));
        c.textIn("印", c.getBox("clinicHanko"), HAlign.Center, VAlign.Center);
    }

}
