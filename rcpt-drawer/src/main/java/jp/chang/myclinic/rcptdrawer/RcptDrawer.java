package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;

import java.util.ArrayList;
import java.util.List;

public class RcptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private List<List<Op>> pages = new ArrayList<>();
    private Box patientIdBox = new Box(100, 2, 150, 8);
    private Box shinryouNenBox;
    private Box shinryouMonthBox;
    private Box fukenbangouBox;
    private Box kikancodeBox;

    public RcptDrawer() {
        setupFonts();
        compiler.setFont("Gothic3");
        compiler.textInJustified("診療報酬明細書", new Box(22, 20, 46, 23), VAlign.Bottom);
        compiler.setFont("Gothic2.2");
        compiler.textIn("(医科入院外)", new Box(31.5, 25, 44, 26.5), HAlign.Left, VAlign.Bottom);
        setupDate();
        setupFukenBangou();
        setupKikanCode();
        setupHokenShubetsu();
    }

    List<List<Op>> getPages() {
        pages.add(compiler.getOps());
        return pages;
    }

    public void putPatientId(int patientId) {
        String s = String.format("%d", patientId);
        compiler.setFont("Gothic2.5");
        compiler.textIn(s, patientIdBox, HAlign.Left, VAlign.Bottom);
    }

    public void putShinryouMonth(int nen, int month) {
        compiler.setFont("Gothic2.8");
        compiler.textIn("" + nen, shinryouNenBox, HAlign.Right, VAlign.Bottom);
        compiler.textIn("" + month, shinryouMonthBox, HAlign.Right, VAlign.Bottom);
    }

    private void setupFonts() {
        compiler.createFont("Gothic4", "MS Gothic", 4);
        compiler.createFont("Gothic3", "MS Gothic", 3);
        compiler.createFont("Gothic2.8", "MS Gothic", 2.8);
        compiler.createFont("Gothic2.5", "MS Gothic", 2.5);
        compiler.createFont("Gothic2.2", "MS Gothic", 2.2);
        compiler.createFont("Mincho2.6", "MS Mincho", 2.6);
        compiler.createFont("Mincho2.5", "MS Mincho", 2.5);
        compiler.createFont("Mincho2.2", "MS Mincho", 2.2);
    }

    private Box shiftCorners(Box box, double dxLeft, double dxTop, double dxRight, double dxBottom) {
        return box.inset(dxLeft, dxTop, -dxRight, -dxBottom);
    }

    private void setupDate() {
        compiler.setFont("Gothic2.5");
        Box box = new Box(64, 26.3, 87, 28.5);
        Box[] cols = box.splitToColumns(10, 17.5);
        compiler.textIn("平成", cols[0], HAlign.Left, VAlign.Bottom);
        compiler.textIn("年", cols[1], HAlign.Left, VAlign.Bottom);
        compiler.textIn("月分", cols[2], HAlign.Left, VAlign.Bottom);
        shinryouNenBox = shiftCorners(cols[0], 5, 0, -1.5, 0);
        shinryouMonthBox = shiftCorners(cols[1], 3, 0, -1.5, 0);
    }

    private void setupFukenBangou() {
        compiler.setFont("Mincho2.5");
        Box box = new Box(94, 26.3, 101.5, 28.5);
        fukenbangouBox = markLowerPart(box, 3);
        compiler.frameBottom(box);
        box = expandToTop(box, 9);
        box = shrinkToTop(box, 5);
        compiler.textInJustified("都道府", box, VAlign.Top);
        compiler.textInJustified("県番号", box, VAlign.Bottom);
    }

    private Box expandToTop(Box box, double height){
        return box.setHeight(height, VertAnchor.Bottom);
    }

    private Box shrinkToTop(Box box, double height){
        return box.setHeight(height, VertAnchor.Top);
    }

    private Box markLowerPart(Box box, double height){
        return box.setHeight(height, VertAnchor.Bottom);
    }

    public void putFukenBangou(int bangou){
        compiler.setFont("Gothic2.8");
        compiler.textIn("" + bangou, fukenbangouBox, HAlign.Center, VAlign.Bottom);
    }

    private void setupKikanCode(){
        compiler.setFont("Mincho2.5");
        Box box = new Box(103, 26.3, 139.5, 28.5);
        kikancodeBox = markLowerPart(box, 3);
        compiler.frameBottom(box);
        box = expandToTop(box, 9);
        compiler.textIn("医療機関コード", box, HAlign.Left, VAlign.Top);
    }

    public void putKikanCode(String kikancode){
        compiler.setFont("Gothic4");
        Box box = kikancodeBox.inset(10, 0, 6, 0);
        compiler.textInJustified(kikancode, box, VAlign.Bottom);
    }

    private void setupHokenShubetsu(){
        Box box = new Box(141, 19, 199, 29.5);
        compiler.box(box);
        Box[] cols = box.splitToColumns(4.5, 25, 35);
        setupHokenShubetsu1(cols[0]);
        setupHokenShubetsu2(cols[1]);
    }

    private void setupHokenShubetsu1(Box box){
        compiler.frameRight(box);
        Box[] rows = box.splitToRows(3.5);
        compiler.frameBottom(rows[0]);
        compiler.setFont("Mincho2.2");
        compiler.textIn("1", rows[0], HAlign.Center, VAlign.Center);
        compiler.setFont("Mincho2.6");
        compiler.textInVertJustified("医科", rows[1].inset(0, 0.8), HAlign.Center);
    }

    private void setupHokenShubetsu2(Box box){
        compiler.frameRight(box);
        Box[] cols = box.splitToEvenColumns(2);

    }

}
