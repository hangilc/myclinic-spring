package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RcptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private List<List<Op>> pages = new ArrayList<>();
    private Box patientIdBox = new Box(100, 2, 150, 8);
    private Box shinryouNenBox;
    private Box shinryouMonthBox;
    private Box fukenbangouBox;
    private Box kikancodeBox;
    private Point hokenshubetsuShakoku;
    private Point hokenshubetsuKouhi1;
    private Point hokenshubetsuRoujin;
    private Point hokenshubetsuTaishoku;
    private Point hokentandokuTandoku;
    private Point hokentandokuHei2;
    private Point hokentandokuHei3;
    private Point hokenfutanHonnin;
    private Point hokenfutanSansai;
    private Point hokenfutanKazoku;
    private Point hokenfutanKourei9;
    private Point hokenfutanKourei7;
    private Box[] hokenshabangouBoxes;
    private Point kyuufuwariaiWari10;
    private Point kyuufuwariaiWari9;
    private Point kyuufuwariaiWari8;
    private Point kyuufuwariaiWari7;
    private Box kyuufuwariaiOther;

    public RcptDrawer() {
        setupFonts();
        setupPens();
        compiler.setPen("regular");
        compiler.setFont("Gothic3");
        compiler.textInJustified("診療報酬明細書", new Box(22, 20, 46, 23), VAlign.Bottom);
        compiler.setFont("Gothic2.2");
        compiler.textIn("(医科入院外)", new Box(31.5, 25, 44, 26.5), HAlign.Left, VAlign.Bottom);
        setupDate();
        setupFukenBangou();
        setupKikanCode();
        setupHokenShubetsu();
        setupHokenshaBangou();
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
        compiler.createFont("Gothic5", "MS Gothic", 5);
        compiler.createFont("Gothic4", "MS Gothic", 4);
        compiler.createFont("Gothic3", "MS Gothic", 3);
        compiler.createFont("Gothic2.8", "MS Gothic", 2.8);
        compiler.createFont("Gothic2.6", "MS Gothic", 2.6);
        compiler.createFont("Gothic2.5", "MS Gothic", 2.5);
        compiler.createFont("Gothic2.3", "MS Gothic", 2.3);
        compiler.createFont("Gothic2.2", "MS Gothic", 2.2);
        compiler.createFont("Mincho2.6", "MS Mincho", 2.6);
        compiler.createFont("Mincho2.5", "MS Mincho", 2.5);
        compiler.createFont("Mincho2.2", "MS Mincho", 2.2);
        compiler.createFont("Mincho2", "MS Mincho", 2);
    }

    private void setupPens() {
        compiler.createPen("regular", 0, 0, 0, 0.1);
        compiler.createPen("dot", 0, 0, 0, 0.1, OpCreatePen.PS_DOT);
        compiler.createPen("bold", 0, 0, 0, 0.2);
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

    private Box expandToTop(Box box, double height) {
        return box.setHeight(height, VertAnchor.Bottom);
    }

    private Box shrinkToTop(Box box, double height) {
        return box.setHeight(height, VertAnchor.Top);
    }

    private Box markLowerPart(Box box, double height) {
        return box.setHeight(height, VertAnchor.Bottom);
    }

    public void putFukenBangou(int bangou) {
        compiler.setFont("Gothic2.8");
        compiler.textIn("" + bangou, fukenbangouBox, HAlign.Center, VAlign.Bottom);
    }

    private void setupKikanCode() {
        compiler.setFont("Mincho2.5");
        Box box = new Box(103, 26.3, 139.5, 28.5);
        kikancodeBox = markLowerPart(box, 3);
        compiler.frameBottom(box);
        box = expandToTop(box, 9);
        compiler.textIn("医療機関コード", box, HAlign.Left, VAlign.Top);
    }

    public void putKikanCode(String kikancode) {
        compiler.setFont("Gothic4");
        Box box = kikancodeBox.inset(10, 0, 6, 0);
        compiler.textInJustified(kikancode, box, VAlign.Bottom);
    }

    private void setupHokenShubetsu() {
        Box box = new Box(141, 19, 199, 29.5);
        compiler.box(box);
        Box[] cols = box.splitToColumns(4.5, 25, 35);
        setupHokenShubetsu1(cols[0]);
        setupHokenShubetsu2(cols[1]);
        setupHokenShubetsu3(cols[2]);
        setupHokenShubetsu4(cols[3]);
    }

    private void setupHokenShubetsu1(Box box) {
        compiler.frameRight(box);
        Box[] rows = box.splitToRows(3.5);
        compiler.frameBottom(rows[0]);
        compiler.setFont("Mincho2.2");
        compiler.textIn("1", rows[0], HAlign.Center, VAlign.Center);
        compiler.setFont("Mincho2.6");
        compiler.textInVertJustified("医科", rows[1].inset(0, 0.8), HAlign.Center);
    }

    private void setupHokenShubetsu2(Box box) {
        compiler.frameRight(box);
        Box[] cols = box.splitToEvenColumns(2);
        setupHokenShubetsu2_1(cols[0]);
        setupHokenShubetsu2_2(cols[1]);
    }

    private void setupHokenShubetsu2_1(Box box) {
        compiler.setPen("dot");
        compiler.frameRight(box);
        compiler.setPen("regular");
        Box[] rows = box.splitToEvenRows(2);
        hokenShubetsu(rows[0], "１", "社・国", point -> this.hokenshubetsuShakoku = point);
        hokenShubetsu(rows[1], "２", "公費", point -> this.hokenshubetsuKouhi1 = point);
    }

    private void markCircle(Point p){
        compiler.setPen("regular");
        compiler.circle(p, 1.5);
    }

    public void markHokenshubetsuShakoku(){
        markCircle(hokenshubetsuShakoku);
    }

    public void markHokenshubetsuKouhi1(){
        markCircle(hokenshubetsuKouhi1);
    }

    private void setupHokenShubetsu2_2(Box box) {
        Box[] rows = box.splitToEvenRows(2);
        hokenShubetsu(rows[0], "３", "後期", point -> this.hokenshubetsuRoujin = point);
        hokenShubetsu(rows[1], "４", "退職", point -> this.hokenshubetsuTaishoku = point);
    }

    public void markHokenshubetsuKoukikourei(){
        markCircle(hokenshubetsuRoujin);
    }

    public void markHokenshubetsuTaishoku(){
        markCircle(hokenshubetsuTaishoku);
    }

    private void hokenShubetsu(Box box, String num, String label, Consumer<Point> cb){
        Box[] cols = box.splitToColumns(2.5);
        compiler.setFont("Gothic2.6");
        compiler.textIn(num, cols[0], HAlign.Center, VAlign.Center);
        compiler.setFont("Mincho2.6");
        compiler.textInJustified(label, cols[1], VAlign.Center);
        cb.accept(box.getCenterPoint());
    }

    private void setupHokenShubetsu3(Box box){
        compiler.frameRight(box);
        Box[] rows = box.splitToEvenRows(3);
        hokenShubetsu(rows[0], "１", "単独", point -> this.hokentandokuTandoku = point);
        hokenShubetsu(rows[1], "２", "２併", point -> this.hokentandokuHei2 = point);
        hokenShubetsu(rows[2], "３", "３併", point -> this.hokentandokuHei3 = point);
    }

    public void markHokentandokuTandoku(){
        markCircle(hokentandokuTandoku);
    }

    public void markHokentandokuHei2(){
        markCircle(hokentandokuHei2);
    }

    public void markHokentandokuHei3(){
        markCircle(hokentandokuHei3);
    }

    private void setupHokenShubetsu4(Box box){
        Box[] cols = box.splitToEvenColumns(2);
        compiler.setPen("dot");
        compiler.frameRight(cols[0]);
        compiler.setPen("regular");
        setupHokenShubetsu4_1(cols[0]);
        setupHokenShubetsu4_2(cols[1]);
    }

    private void setupHokenShubetsu4_1(Box box) {
        Box[] rows = box.splitToEvenRows(3);
        hokenShubetsu(rows[0], "２", "本外", point -> this.hokenfutanHonnin = point);
        hokenShubetsu(rows[1], "４", "六外", point -> this.hokenfutanSansai = point);
        hokenShubetsu(rows[2], "６", "家外", point -> this.hokenfutanKazoku = point);
    }

    public void markHokenfutanHonnin(){
        markCircle(hokenfutanHonnin);
    }

    public void markHokenfutanSansai(){
        markCircle(hokenfutanSansai);
    }

    public void markHokenfutanKazoku(){
        markCircle(hokenfutanKazoku);
    }

    private void setupHokenShubetsu4_2(Box box) {
        Box[] rows = box.splitToEvenRows(2);
        hokenShubetsu(rows[0], "８", "高外一", point -> this.hokenfutanKourei9 = point);
        hokenShubetsu(rows[1], "０", "高外７", point -> this.hokenfutanKourei7 = point);
   }

    public void markHokenfutanKourei9(){
        markCircle(hokenfutanKourei9);
    }

    public void markHokenfutanKourei7(){
        markCircle(hokenfutanKourei7);
    }

    public void setupHokenshaBangou(){
        Box box = new Box(115, 29.5, 199, 38);
        compiler.box(box);
        Box[] cols = box.splitToColumns(8.5, 68.5, 71.5);
        setupHokenshaBangou_1(cols[0]);
        setupHokenshaBangou_2(cols[1]);
        setupHokenshaBangou_3(cols[2]);
        setupHokenshaBangou_4(cols[3]);
    }

    private void setupHokenshaBangou_1(Box box){
        compiler.frameRight(box);
        Box[] rows = box.splitToEvenRows(2);
        compiler.setFont("Mincho2");
        compiler.textInJustified("保険者", rows[0], VAlign.Center);
        compiler.textInJustified("番号", rows[1], VAlign.Center);
    }

    private void setupHokenshaBangou_2(Box box){
        Box[] cols = box.splitToEvenColumns(8);
        compiler.frameRight(box);
        compiler.setPen("dot");
        List.of(0, 2, 4, 5).forEach(i -> {
            compiler.frameRight(cols[i]);
        });
        compiler.setPen("bold");
        compiler.frameRight(cols[1]);
        compiler.box(Box.boundingBox(cols[4], cols[6]));
        compiler.setPen("regular");
        this.hokenshabangouBoxes = cols;
    }

    public void setHokenshaBangou(int n){
        String s = String.format("%d", n);
        if( s.length() > 8 ){
            throw new RuntimeException("Too large hokensha bangou: " + n);
        }
        int offset = 8 - s.length();
        compiler.setFont("Gothic5");
        for(int i=0;i<s.length();i++){
            compiler.textIn(s.substring(i,i+1), hokenshabangouBoxes[i+offset],
                    HAlign.Center, VAlign.Center);
        }
    }

    private void setupHokenshaBangou_3(Box box){
        compiler.frameRight(box);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("給付割合", box, HAlign.Center);
    }

    private void setupHokenshaBangou_4(Box box){
        compiler.setFont("Mincho2");
        box = box.inset(1, 1);
        Box[] rows = box.splitToVerticallyJustifiedRows(2.0, 2);
        {
            Box[] cols = rows[0].splitToHorizontallyJustifiedColumns(2, 3);
            compiler.textIn("10", cols[0], HAlign.Center, VAlign.Center);
            kyuufuwariaiWari10 = cols[0].getCenterPoint();
            compiler.textIn("9", cols[1], HAlign.Center, VAlign.Center);
            kyuufuwariaiWari9 = cols[1].getCenterPoint();
            compiler.textIn("8", cols[2], HAlign.Center, VAlign.Center);
            kyuufuwariaiWari8 = cols[2].getCenterPoint();
        }
        {
            Box row = rows[1];
            Box left = row.setWidth(2, Box.HorizAnchor.Left);
            compiler.textIn("7", left, HAlign.Center, VAlign.Center);
            kyuufuwariaiWari7 = left.getCenterPoint();
            Box right = row.setWidth(7, Box.HorizAnchor.Right);
            compiler.textInJustified("()", right, VAlign.Center);
            kyuufuwariaiOther = right.inset(1, 0);
        }
    }

    public void markKyuufuwari10(){
        markCircle(kyuufuwariaiWari10);
    }

    public void markKyuufuwari9(){
        markCircle(kyuufuwariaiWari9);
    }

    public void markKyuufuwari8(){
        markCircle(kyuufuwariaiWari8);
    }

    public void markKyuufuwari7(){
        markCircle(kyuufuwariaiWari7);
    }

    public void putKyuufuwariOther(String s){
        compiler.setFont("Gothic2.3");
        compiler.textIn(s, kyuufuwariaiOther, HAlign.Center, VAlign.Bottom);
    }

}
