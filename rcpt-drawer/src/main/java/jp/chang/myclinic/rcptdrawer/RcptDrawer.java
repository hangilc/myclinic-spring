package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
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
    private Box hihokenshashou;
    private Box[] kouhifutanshabangou1;
    private Box[] kouhijukyuushabangou1;
    private Box[] kouhifutanshabangou2;
    private Box[] kouhijukyuushabangou2;
    private Box shimei;
    private Point seibetsuOtoko;
    private Point seibetsuOnna;
    private Point seinengappiMeiji;
    private Point seinengappiTaishou;
    private Point seinengappiShouwa;
    private Point seinengappiHeisei;
    private Box seinengappiNen;
    private Box seinengappiMonth;
    private Box seinengappiDay;
    private Point shokumujouShokumujou;
    private Point shokumujouGesen;
    private Point shokumujouTsuukin;
    private Box tokkijikou;
    private Box shozaichimeishouLine1;
    private Box shozaichimeishouLine2;
    private Box shozaichimeishouLine3;
    private Box[] shoubyoumeiNumbers;
    private Box[] shoubyoumeiTexts;

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
        setupHihokensha();
        setupKouhi();
        setupPatientInfo();
        setupIryouKikanLabel();
        setupShozaichiMeishou();
        setupRcptBody();
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
        compiler.createFont("Gothic2", "MS Gothic", 2);
        compiler.createFont("Mincho4.5", "MS Mincho", 4.5);
        compiler.createFont("Mincho3.5", "MS Mincho", 3.5);
        compiler.createFont("Mincho2.6", "MS Mincho", 2.6);
        compiler.createFont("Mincho2.5", "MS Mincho", 2.5);
        compiler.createFont("Mincho2.3", "MS Mincho", 2.3);
        compiler.createFont("Mincho2.2", "MS Mincho", 2.2);
        compiler.createFont("Mincho2", "MS Mincho", 2);
        compiler.createFont("Mincho1.8", "MS Mincho", 1.8);
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

    private void markCircle(Point p) {
        compiler.setPen("regular");
        compiler.circle(p, 1.5);
    }

    public void markHokenshubetsuShakoku() {
        markCircle(hokenshubetsuShakoku);
    }

    public void markHokenshubetsuKouhi1() {
        markCircle(hokenshubetsuKouhi1);
    }

    private void setupHokenShubetsu2_2(Box box) {
        Box[] rows = box.splitToEvenRows(2);
        hokenShubetsu(rows[0], "３", "後期", point -> this.hokenshubetsuRoujin = point);
        hokenShubetsu(rows[1], "４", "退職", point -> this.hokenshubetsuTaishoku = point);
    }

    public void markHokenshubetsuKoukikourei() {
        markCircle(hokenshubetsuRoujin);
    }

    public void markHokenshubetsuTaishoku() {
        markCircle(hokenshubetsuTaishoku);
    }

    private void hokenShubetsu(Box box, String num, String label, Consumer<Point> cb) {
        Box[] cols = box.splitToColumns(2.5);
        compiler.setFont("Gothic2.6");
        compiler.textIn(num, cols[0], HAlign.Center, VAlign.Center);
        compiler.setFont("Mincho2.6");
        compiler.textInJustified(label, cols[1], VAlign.Center);
        cb.accept(box.getCenterPoint());
    }

    private void setupHokenShubetsu3(Box box) {
        compiler.frameRight(box);
        Box[] rows = box.splitToEvenRows(3);
        hokenShubetsu(rows[0], "１", "単独", point -> this.hokentandokuTandoku = point);
        hokenShubetsu(rows[1], "２", "２併", point -> this.hokentandokuHei2 = point);
        hokenShubetsu(rows[2], "３", "３併", point -> this.hokentandokuHei3 = point);
    }

    public void markHokentandokuTandoku() {
        markCircle(hokentandokuTandoku);
    }

    public void markHokentandokuHei2() {
        markCircle(hokentandokuHei2);
    }

    public void markHokentandokuHei3() {
        markCircle(hokentandokuHei3);
    }

    private void setupHokenShubetsu4(Box box) {
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

    public void markHokenfutanHonnin() {
        markCircle(hokenfutanHonnin);
    }

    public void markHokenfutanSansai() {
        markCircle(hokenfutanSansai);
    }

    public void markHokenfutanKazoku() {
        markCircle(hokenfutanKazoku);
    }

    private void setupHokenShubetsu4_2(Box box) {
        Box[] rows = box.splitToEvenRows(2);
        hokenShubetsu(rows[0], "８", "高外一", point -> this.hokenfutanKourei9 = point);
        hokenShubetsu(rows[1], "０", "高外７", point -> this.hokenfutanKourei7 = point);
    }

    public void markHokenfutanKourei9() {
        markCircle(hokenfutanKourei9);
    }

    public void markHokenfutanKourei7() {
        markCircle(hokenfutanKourei7);
    }

    public void setupHokenshaBangou() {
        Box box = new Box(115, 29.5, 199, 38);
        compiler.box(box);
        Box[] cols = box.splitToColumns(8.5, 68.5, 71.5);
        setupHokenshaBangou_1(cols[0]);
        setupHokenshaBangou_2(cols[1]);
        setupHokenshaBangou_3(cols[2]);
        setupHokenshaBangou_4(cols[3]);
    }

    private void setupHokenshaBangou_1(Box box) {
        compiler.frameRight(box);
        Box[] rows = box.splitToEvenRows(2);
        compiler.setFont("Mincho2");
        compiler.textInJustified("保険者", rows[0], VAlign.Center);
        compiler.textInJustified("番号", rows[1], VAlign.Center);
    }

    private void setupHokenshaBangou_2(Box box) {
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

    public void setHokenshaBangou(int n) {
        compiler.setFont("Gothic5");
        printDigits(n, hokenshabangouBoxes);
    }

    private void setupHokenshaBangou_3(Box box) {
        compiler.frameRight(box);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("給付割合", box, HAlign.Center);
    }

    private void setupHokenshaBangou_4(Box box) {
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

    public void markKyuufuwari10() {
        markCircle(kyuufuwariaiWari10);
    }

    public void markKyuufuwari9() {
        markCircle(kyuufuwariaiWari9);
    }

    public void markKyuufuwari8() {
        markCircle(kyuufuwariaiWari8);
    }

    public void markKyuufuwari7() {
        markCircle(kyuufuwariaiWari7);
    }

    public void putKyuufuwariOther(String s) {
        compiler.setFont("Gothic2.3");
        compiler.textIn(s, kyuufuwariaiOther, HAlign.Center, VAlign.Bottom);
    }

    private void setupHihokensha() {
        Box box = new Box(115, 39, 199, 39 + 11.5);
        compiler.box(box);
        Box[] cols = box.splitToColumns(30.5);
        compiler.frameRight(cols[0]);
        Box[] rows = cols[0].inset(1.5, 3).splitToEvenRows((2));
        compiler.setFont("Mincho2");
        compiler.textInJustified("被保険者証・被保険者", rows[0], VAlign.Top);
        compiler.textInJustified("手帳等の記号・番号", rows[1], VAlign.Top);
        this.hihokenshashou = cols[1];
    }

    public void putHihokenshashou(String s) {
        compiler.setFont("Gothic4");
        compiler.textIn(s, hihokenshashou.displaceLeftEdge(3), HAlign.Left, VAlign.Center);
    }

    private void setupKouhi() {
        Box box = new Box(17, 29.5, 105.5, 52.5);
        compiler.box(box);
        Box[] rows = box.splitToEvenRows(3);
        compiler.frameBottom(rows[0]);
        compiler.frameBottom(rows[1]);
        double[] xs = new double[]{8, 47, 56.5};
        {
            Box[] cols = box.splitToColumns(xs);
            compiler.frameRight(cols[0]);
            {
                Box[] cs = cols[1].splitToEvenColumns(8);
                compiler.setPen("dot");
                List.of(0, 2, 4, 5).forEach(i -> compiler.frameRight(cs[i]));
                compiler.setPen("bold");
                compiler.frameRight(cs[1]);
                compiler.box(Box.boundingBox2(cs[4], cs[6]));
                compiler.setPen("regular");
            }
            compiler.frameRight(cols[1]);
            compiler.frameRight(cols[2]);
            {
                Box[] cs = cols[3].splitToEvenColumns(7);
                compiler.setPen("dot");
                List.of(0, 1, 3, 4).forEach(i -> compiler.frameRight(cs[i]));
                compiler.setPen("bold");
                compiler.box(Box.boundingBox2(cs[3], cs[5]));
                compiler.setPen("regular");
            }
        }
        compiler.setFont("Mincho2.6");
        {
            Box[] cols = rows[0].splitToColumns(xs);
            compiler.textIn("－", cols[0], HAlign.Center, VAlign.Center);
            compiler.textIn("－", cols[2], HAlign.Center, VAlign.Center);
        }
        {
            Box[] cols = rows[1].splitToColumns(xs);
            Box[] rs = cols[0].splitToEvenRows(3);
            compiler.textInJustified("公費負", rs[0], VAlign.Bottom);
            compiler.textInJustified("担者番", rs[1], VAlign.Bottom);
            compiler.textInJustified("号①", rs[2], VAlign.Bottom);
            rs = cols[2].splitToEvenRows(3);
            compiler.textInJustified("公費負担", rs[0], VAlign.Bottom);
            compiler.textInJustified("医療の受", rs[1], VAlign.Bottom);
            compiler.setFont("Mincho1.8");
            compiler.textInJustified("給者番号①", rs[2], VAlign.Bottom);
            compiler.setFont("Mincho2.6");
            this.kouhifutanshabangou1 = cols[1].splitToEvenColumns(8);
            this.kouhijukyuushabangou1 = cols[3].splitToEvenColumns(7);
        }
        {
            Box[] cols = rows[2].splitToColumns(xs);
            Box[] rs = cols[0].splitToEvenRows(3);
            compiler.textInJustified("公費負", rs[0], VAlign.Bottom);
            compiler.textInJustified("担者番", rs[1], VAlign.Bottom);
            compiler.textInJustified("号②", rs[2], VAlign.Bottom);
            rs = cols[2].splitToEvenRows(3);
            compiler.textInJustified("公費負担", rs[0], VAlign.Bottom);
            compiler.textInJustified("医療の受", rs[1], VAlign.Bottom);
            compiler.setFont("Mincho1.8");
            compiler.textInJustified("給者番号②", rs[2], VAlign.Bottom);
            compiler.setFont("Mincho2.6");
            this.kouhifutanshabangou2 = cols[1].splitToEvenColumns(8);
            this.kouhijukyuushabangou2 = cols[3].splitToEvenColumns(7);
        }
    }

    public void putKouhiFutanshaBangou1(int n){
        compiler.setFont("Gothic5");
        printDigits(n, kouhifutanshabangou1);
    }

    public void putKouhiJukyuushaBangou1(int n){
        compiler.setFont("Gothic5");
        printDigits(n, kouhijukyuushabangou1);
    }

    public void putKouhiFutanshaBangou2(int n){
        compiler.setFont("Gothic5");
        printDigits(n, kouhifutanshabangou2);
    }

    public void putKouhiJukyuushaBangou2(int n){
        compiler.setFont("Gothic5");
        printDigits(n, kouhijukyuushabangou2);
    }

    private void printDigits(int n, Box[] boxes){
        String s = String.format("%d", n);
        if( s.length() > boxes.length ){
            throw new RuntimeException("Too large number: " + n);
        }
        int offset = boxes.length - s.length();
        for(int i=0;i<s.length();i++){
            String c = s.substring(i, i+1);
            compiler.textIn(c, boxes[offset+i], HAlign.Center, VAlign.Center);
        }
    }

    private void setupPatientInfo(){
        Box box = new Box(17, 55.5, 106.5, 76.5);
        compiler.box(box);
        Box[] cols = box.splitToColumns(68.5);
        compiler.frameRight(cols[0]);
        {
            Box r = cols[0];
            Box[] rows = r.splitToRows(15);
            setupPatientInfo_Names(rows[0]);
            setupPatientInfo_Shokumujou(rows[1]);
        }
        setupPatientInfo_Tokkijikou(cols[1]);
    }

    private void setupPatientInfo_Names(Box box){
        compiler.frameBottom(box);
        Box[] cols = box.splitToColumns(3.5);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("氏名", cols[0].inset(0, 3), HAlign.Center);
        this.shimei = cols[1].shrinkHeight(2, VertAnchor.Top);
        Box r = cols[1].setHeight(2, VertAnchor.Bottom);
        Box r1 = setBottomCenterAt(r, 2.2, 2);
        compiler.textIn("1", r1, HAlign.Center, VAlign.Bottom);
        this.seibetsuOtoko = r1.getCenterPoint();
        Box r2 = r.displaceLeftEdge(4).setWidth(4, Box.HorizAnchor.Left);
        compiler.textIn("男", r2, HAlign.Left, VAlign.Bottom);
        r1 = r1.shiftToRight(6.3);
        r2 = r2.shiftToRight(6.3);
        compiler.textIn("2", r1, HAlign.Center, VAlign.Bottom);
        this.seibetsuOnna = r1.getCenterPoint();
        compiler.textIn("女", r2, HAlign.Left, VAlign.Bottom);
        r1 = r1.shiftToRight(7);
        r2 = r2.shiftToRight(7);
        compiler.textIn("1", r1, HAlign.Center, VAlign.Bottom);
        compiler.textIn("明", r2, HAlign.Left, VAlign.Bottom);
        this.seinengappiMeiji = r1.getCenterPoint();
        r1 = r1.shiftToRight(7);
        r2 = r2.shiftToRight(7);
        compiler.textIn("2", r1, HAlign.Center, VAlign.Bottom);
        compiler.textIn("大", r2, HAlign.Left, VAlign.Bottom);
        this.seinengappiTaishou = r1.getCenterPoint();
        r1 = r1.shiftToRight(7);
        r2 = r2.shiftToRight(7);
        compiler.textIn("3", r1, HAlign.Center, VAlign.Bottom);
        compiler.textIn("昭", r2, HAlign.Left, VAlign.Bottom);
        this.seinengappiShouwa = r1.getCenterPoint();
        r1 = r1.shiftToRight(7);
        r2 = r2.shiftToRight(7);
        compiler.textIn("4", r1, HAlign.Center, VAlign.Bottom);
        compiler.textIn("平", r2, HAlign.Left, VAlign.Bottom);
        this.seinengappiHeisei = r1.getCenterPoint();
        r1 = setBottomCenterAt(r, 47, 2);
        compiler.textIn("・", r1, HAlign.Center, VAlign.Center);
        markLeft(r1, 5, b -> this.seinengappiNen = b);
        r1 = setBottomCenterAt(r, 53, 2);
        compiler.textIn("・", r1, HAlign.Center, VAlign.Center);
        markLeft(r1, 5, b -> this.seinengappiMonth = b);
        r1 = setBottomCenterAt(r, 60, 2);
        compiler.textIn("生", r1, HAlign.Center, VAlign.Center);
        markLeft(r1, 5, b -> this.seinengappiDay = b);
    }

    private void setupPatientInfo_Shokumujou(Box box){
        Box[] cols = box.splitToColumns(16);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInJustified("職務上の事由", cols[0], VAlign.Center);
        Box r2 = cols[1].setHeight(2, VertAnchor.Center);
        makeIndexAndLabel(r2, 3, 2, 1.5, 15, (rIndex, rLabel) -> {
            compiler.textIn("1", rIndex, HAlign.Center, VAlign.Bottom);
            compiler.textIn("職務上", rLabel, HAlign.Left, VAlign.Bottom);
            this.shokumujouShokumujou = rIndex.getCenterPoint();
            rIndex = rIndex.shiftToRight(13);
            rLabel = rLabel.shiftToRight(13);
            compiler.textIn("2", rIndex, HAlign.Center, VAlign.Bottom);
            compiler.textIn("下船後３月以内", rLabel, HAlign.Left, VAlign.Bottom);
            this.shokumujouGesen = rIndex.getCenterPoint();
            rIndex = rIndex.shiftToRight(22);
            rLabel = rLabel.shiftToRight(22);
            compiler.textIn("3", rIndex, HAlign.Center, VAlign.Bottom);
            compiler.textIn("通勤災害", rLabel, HAlign.Left, VAlign.Bottom);
            this.shokumujouTsuukin = rIndex.getCenterPoint();
        });
    }

    private void setupPatientInfo_Tokkijikou(Box box){
        Box[] rows = box.splitToRows(4);
        compiler.frameBottom(rows[0]);
        compiler.setFont("Mincho2");
        compiler.textInJustified("特記事項", rows[0].inset(3.5, 0), VAlign.Center);
        this.tokkijikou = rows[1];
    }

    public void putShimei(String s){
        compiler.setFont("Gothic4");
        compiler.textIn(s, shimei.displaceLeftEdge(3), HAlign.Left, VAlign.Center);
    }

    public void markSeibetsuOtoko(){
        markCircle(seibetsuOtoko);
    }

    public void markSeibetsuOnna(){
        markCircle(seibetsuOnna);
    }

    public void markSeinengappiMeiji(){
        markCircle(seinengappiMeiji);
    }

    public void markSeinengappiTaishou(){
        markCircle(seinengappiTaishou);
    }

    public void markSeinengappiShouwa(){
        markCircle(seinengappiShouwa);
    }

    public void markSeinengappiHeisei(){
        markCircle(seinengappiHeisei);
    }

    public void putSeinengappiNen(int nen){
        compiler.setFont("Gothic2");
        compiler.textIn("" + nen, seinengappiNen.displaceRightEdge(-1),
                HAlign.Right, VAlign.Bottom);
    }

    public void putSeinengappiMonth(int month){
        compiler.setFont("Gothic2");
        compiler.textIn("" + month, seinengappiMonth.displaceRightEdge(-1),
                HAlign.Right, VAlign.Bottom);
    }

    public void putSeinengappiDay(int day){
        compiler.setFont("Gothic2");
        compiler.textIn("" + day, seinengappiDay.displaceRightEdge(-1),
                HAlign.Right, VAlign.Bottom);
    }

    public void markShokumujouShokumujou(){
        markCircle(shokumujouShokumujou);
    }

    public void markShokumujouGesen(){
        markCircle(shokumujouGesen);
    }

    public void markShokumujouTsuukin(){
        markCircle(shokumujouTsuukin);
    }

    public void putTokkijikou(String text){
        compiler.setFont("Mincho2.5");
        String[] lines = text.split("\\r\\n|\\n");
        compiler.paragraph(text, tokkijikou.inset(1, 1), HAlign.Left, VAlign.Top, 0);
    }

    private Box setBottomCenterAt(Box box, double x, double width){
        double c = box.getLeft() + x;
        return new Box(c - width/2, box.getTop(), c + width/2, box.getBottom());
    }

    private void markLeft(Box box, double width, Consumer<Box> cb){
        cb.accept(box.flipLeft().setWidth(width, Box.HorizAnchor.Right));
    }

    private void makeIndexAndLabel(Box box, double indexCenter, double indexWidth, double offset,
                              double labelWidth, BiConsumer<Box, Box> cb){
        Box rIndex = setBottomCenterAt(box, indexCenter, indexWidth);
        Box rLabel = box.shiftToRight(indexCenter + offset).setWidth(labelWidth, Box.HorizAnchor.Left);
        cb.accept(rIndex, rLabel);
    }

    private void setupIryouKikanLabel(){
        Box box = new Box(110, 57, 120, 73.5);
        Box[] rows = box.splitToVerticallyJustifiedRows(2, 5);
        compiler.setFont("Mincho2");
        compiler.textInJustified("保険医", rows[0], VAlign.Top);
        compiler.textInJustified("療機関", rows[1], VAlign.Top);
        compiler.textInJustified("の所在", rows[2], VAlign.Top);
        compiler.textInJustified("地及び", rows[3], VAlign.Top);
        compiler.textInJustified("名称", rows[4], VAlign.Top);
    }

    private void setupShozaichiMeishou(){
        Box box = new Box(122, 57, 199, 73.5);
        Box[] rows = box.splitToRows(4, 10);
        this.shozaichimeishouLine1 = rows[0];
        this.shozaichimeishouLine2 = rows[1];
        this.shozaichimeishouLine3 = rows[2];
        Box r = new Box(179, 73, 197, 75);
        compiler.setFont("Mincho2");
        compiler.textIn("（", r, HAlign.Left, VAlign.Top);
        compiler.textIn("床）", r, HAlign.Right, VAlign.Top);
    }

    public void putShozaichiMeishouLine1(String s){
        compiler.setFont("Mincho3.5");
        compiler.textIn(s, shozaichimeishouLine1, HAlign.Left, VAlign.Top);
    }

    public void putShozaichiMeishouLine2(String s){
        compiler.setFont("Mincho3.5");
        compiler.textIn(s, shozaichimeishouLine2.displaceLeftEdge(28), HAlign.Left, VAlign.Top);
    }

    public void putShozaichiMeishouLine3(String s){
        compiler.setFont("Mincho4.5");
        compiler.textIn(s, shozaichimeishouLine3.displaceLeftEdge(7), HAlign.Left, VAlign.Top);
    }

    private void setupRcptBody(){
        Box box = new Box(17, 76.5, 199, 263);
        compiler.box(box);
        Box[] rows = box.splitToRows(21, 159);
        setupRcptBodyRow1(rows[0]);
        setupRcptBodyRow2(rows[1]);
        setupRcptBodyRow3(rows[2]);
    }

    private void setupRcptBodyRow1(Box box){
        compiler.frameBottom(box);
        Box[] cols = box.splitToColumns(100, 136, 160);
        setupRcptBodyRow1_Shoubyoumei(cols[0]);
        setupRcptBodyRow1_ShinryouKaishi(cols[1]);
        setupRcptBodyRow1_Tenki(cols[2]);
        setupRcptBodyRow1_Nissuu(cols[3]);
    }

    private void setupRcptBodyRow1_Shoubyoumei(Box box){
        compiler.frameRight(box);
        Box[] cols = box.splitToColumns(5);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("傷病名", cols[0].inset(0, 6), HAlign.Center);
        Box r2 = cols[1].inset(2, 3);
        Box[] rows = r2.splitToVerticallyJustifiedRows(2.3, 4);
        shoubyoumeiNumbers = new Box[4];
        shoubyoumeiTexts = new Box[4];
        compiler.setFont("Mincho2.3");
        for(int i=0;i<4;i++){
            Box r = rows[i];
            Box[] rr = r.splitToColumns(4);
            if( i < 3 ){
                String label = String.format("(%d)", i+1);
                compiler.textIn(label, rr[0], HAlign.Left, VAlign.Bottom);
            }
            shoubyoumeiNumbers[i] = rr[0];
            shoubyoumeiTexts[i] = rr[1];
        }
    }

    private void setupRcptBodyRow1_ShinryouKaishi(Box box){

    }

    private void setupRcptBodyRow1_Tenki(Box box){

    }

    private void setupRcptBodyRow1_Nissuu(Box box){

    }

    private void setupRcptBodyRow2(Box box){

    }

    private void setupRcptBodyRow3(Box box){

    }

}
