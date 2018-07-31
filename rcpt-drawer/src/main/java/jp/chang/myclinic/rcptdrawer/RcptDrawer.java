package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.Box.HorizAnchor;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.rcptdrawer.tekiyou.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RcptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private List<List<Op>> pages = new ArrayList<>();
    private Box patientIdBox = new Box(100, 2, 150, 8);
    private Box shinryouNenBox;
    private Box shinryouMonthBox;
    private Box fukenbangouBox;
    private Box kikancodeBox;
    private Point hokenshubetsuShakoku;
    private Point hokenshubetsuKouhi;
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
    private Box[] shoubyoumeiKaishiNumbers = new Box[4];
    private Box[] shoubyoumeiKaishiNen = new Box[4];
    private Box[] shoubyoumeiKaishiMonths = new Box[4];
    private Box[] shoubyoumeiKaishiDays = new Box[4];
    private Box[] shoubyoumeiKaishiNenLabels = new Box[4];
    private Box[] shoubyoumeiKaishiMonthLabels = new Box[4];
    private Box[] shoubyoumeiKaishiDayLabels = new Box[4];
    private Point tenkiChiyuMidashi;
    private Box tenkiChiyu;
    private Point tenkiShibouMidashi;
    private Box tenkiShibou;
    private Point tenkiChuushiMidashi;
    private Box tenkiChuushi;
    private Box shinryounissuHoken;
    private Box shinryounissuKouhi1;
    private Box shinryounissuKouhi2;
    private Box tekiyou;
    private int tekiyouLeftColumnWidth = 10;
    private int tekiyouRightColumnWidth = 10;
    private Point shoshinJikangai;
    private Point shoshinKyuujitsu;
    private Point shoshinShinya;
    private Box shoshinKai;
    private Box shoshinTen;
    private Box saishinSaishinTanka;
    private Box saishinSaishinTimes;
    private Box saishinSaishinTen;
    private Box saishinGairaiKanriTanka;
    private Box saishinGairaiKanriTimes;
    private Box saishinGairaiKanriTen;
    private Box saishinJikangaiTanka;
    private Box saishinJikangaiTimes;
    private Box saishinJikangaiTen;
    private Box saishinKyuujitsuTanka;
    private Box saishinKyuujitsuTimes;
    private Box saishinKyuujitsuTen;
    private Box saishinShinyaTanka;
    private Box saishinShinyaTimes;
    private Box saishinShinyaTen;
    private Box shidouTen;
    private Box zaitakuOushinTanka;
    private Box zaitakuOushinTimes;
    private Box zaitakuOushinTen;
    private Box zaitakuYakanTanka;
    private Box zaitakuYakanTimes;
    private Box zaitakuYakanTen;
    private Box zaitakuShinyaTanka;
    private Box zaitakuShinyaTimes;
    private Box zaitakuShinyaTen;
    private Box zaitakuZaitakuTanka;
    private Box zaitakuZaitakuTimes;
    private Box zaitakuZaitakuTen;
    private Box zaitakuSonotaTen;
    private Box zaitakuYakuzaiTen;
    private Box touyakuNaifukuYakuzaiTimes;
    private Box touyakuNaifukuYakuzaiTen;
    private Box touyakuNaifukuChouzaiTanka;
    private Box touyakuNaifukuChouzaiTimes;
    private Box touyakuNaifukuChouzaiTen;
    private Box touyakuTonpukuYakuzaiTimes;
    private Box touyakuTonpukuYakuzaiTen;
    private Box touyakuGaiyouYakuzaiTimes;
    private Box touyakuGaiyouYakuzaiTen;
    private Box touyakuGaiyouChouzaiTanka;
    private Box touyakuGaiyouChouzaiTimes;
    private Box touyakuGaiyouChouzaiTen;
    private Box touyakuShohouTanka;
    private Box touyakuShohouTimes;
    private Box touyakuShohouTen;
    private Box touyakuMadokuTimes;
    private Box touyakuMadokuTen;
    private Box touyakuChoukiTen;
    private Box chuushaHikaTimes;
    private Box chuushaHikaTen;
    private Box chuushaJoumyakuTimes;
    private Box chuushaJoumyakuTen;
    private Box chuushaSonotaTimes;
    private Box chuushaSonotaTen;
    private Box shochiTimes;
    private Box shochiTen;
    private Box shochiYakuzaiTen;
    private Box shujutsuTimes;
    private Box shujutsuTen;
    private Box shujutsuYakuzaiTen;
    private Box kensaTimes;
    private Box kensaTen;
    private Box kensaYakuzaiTen;
    private Box gazouTimes;
    private Box gazouTen;
    private Box gazouYakuzaiTen;
    private Box sonotaShohousenTimes;
    private Box sonotaShohousenTen;
    private Box sonotaSonotaTen;
    private Box sonotaYakuzaiTen;
    private Box kyuufuHokenSeikyuuten;
    private Box kyuufuKouhi1Seikyuuten;
    private Box kyuufuKouhi2Seikyuuten;
    private Box ichibufutankinHoken;
    private Box ichibufutankinKouhi1;
    private Box ichibufutankinKouhi2;
    private Box shoubyoumeiContinuesToTekiyou;
    private Box zokushiPresent;
    private List<Tekiyou> extraShoubyoumeiList = new ArrayList<>();
    private List<Tekiyou> tekiyouLines = new ArrayList<>();
    private List<Tekiyou> shoujoushoukiList = new ArrayList<>();
    private List<Op> background;
    private List<Runnable> identityProcs = new ArrayList<>();
    private String[] drugBegin;
    private List<String> tekiyouDrugs = new ArrayList<>();

    private static class DataValues {
        Integer patientId;
        String shimei;
    }

    private DataValues dataValues = new DataValues();

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
        this.background = compiler.getOps();
        compiler.clearOps();
    }

    List<List<Op>> getPages() {
        handleTekiyou();
        flushOps();
        if( pages.size() > 1 ){
            compiler.setOps(pages.get(0));
            compiler.setFont("Gothic2.5");
            compiler.textIn("続紙あり", zokushiPresent, HAlign.Right, VAlign.Center);
        }
        for (int i = 1; i < pages.size(); i++) {
            setZokushi(pages.get(i), i, pages.size() - 1);
        }
        return pages;
    }

    private void flushOps() {
        List<Op> page = new ArrayList<>();
        identityProcs.forEach(Runnable::run);
        page.addAll(background);
        page.addAll(compiler.getOps());
        pages.add(page);
        compiler.clearOps();
    }

    private void handleTekiyou() {
        TekiyouContext ctx = new TekiyouContext(compiler, tekiyou, this::flushOps);
        extraShoubyoumeiList.forEach(t -> t.render(ctx));
        tekiyouLines.forEach(t -> t.render(ctx));
        if( shoujoushoukiList.size() > 0 ){
            new TekiyouNop().render(ctx);
//            TekiyouLine title = new TekiyouLine("", "【症状詳記】", "");
//            title.leftMargin = -10;
//            allLines.add(title);
//            allLines.addAll(shoujoushoukiList);
        }
        extraShoubyoumeiList = new ArrayList<>();
        tekiyouLines = new ArrayList<>();
        shoujoushoukiList = new ArrayList<>();
    }

    private void setZokushi(List<Op> target, int zokushiIndex, int zokushiTotal) {
        compiler.setOps(target);
        String font = compiler.getCurrentFont();
        compiler.setFont("Gothic6");
        String text = "続紙";
        if (zokushiTotal > 1) {
            text += String.format("(%d)", zokushiIndex);
            if (zokushiIndex == zokushiTotal) {
                text += "[最後]";
            }
        }
        Box r = shoubyoumeiTexts[0].shrinkHeight(3, VertAnchor.Bottom);
        compiler.textIn(text, r, HAlign.Center, VAlign.Top);
        doPutShimei(dataValues.shimei);
        compiler.setFont(font);
    }

    public int getPatientId() {
        return dataValues.patientId;
    }

    public void clear() {
        compiler.clearOps();
        pages = new ArrayList<>();
        identityProcs = new ArrayList<>();
        dataValues = new DataValues();
    }

    public void putPatientId(int patientId) {
        String s = String.format("%d", patientId);
        dataValues.patientId = patientId;
        identityProcs.add(() -> {
            compiler.setFont("Gothic2.5");
            compiler.textIn(s, patientIdBox, HAlign.Left, VAlign.Bottom);
        });
    }

    public void putShinryouNen(int nen) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic2.8");
            compiler.textIn("" + nen, shinryouNenBox, HAlign.Right, VAlign.Bottom);
        });
    }

    public void putShinryouMonth(int month) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic2.8");
            compiler.textIn("" + month, shinryouMonthBox, HAlign.Right, VAlign.Bottom);
        });
    }

    private void setupFonts() {
        compiler.createFont("Gothic6", "MS Gothic", 6);
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
        compiler.createFont("Mincho1.6", "MS Mincho", 1.6);
        compiler.createFont("Mincho1.5", "MS Mincho", 1.5);
        compiler.createFont("Mincho1.1", "MS Mincho", 1.1);
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
        identityProcs.add(() -> {
            compiler.setFont("Gothic2.8");
            compiler.textIn("" + bangou, fukenbangouBox, HAlign.Center, VAlign.Bottom);
        });
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
        identityProcs.add(() -> {
            compiler.setFont("Gothic4");
            Box box = kikancodeBox.inset(10, 0, 6, 0);
            compiler.textInJustified(kikancode, box, VAlign.Bottom);
        });
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
        hokenShubetsu(rows[1], "２", "公費", point -> this.hokenshubetsuKouhi = point);
    }

    private void markCircle(Point p) {
        compiler.setPen("regular");
        compiler.circle(p, 1.5);
    }

    public void markHokenshubetsuShakoku() {
        markCircle(hokenshubetsuShakoku);
    }

    public void markHokenshubetsuKouhi() {
        markCircle(hokenshubetsuKouhi);
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
        cb.accept(cols[0].getCenterPoint());
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

    public void putHokenshaBangou(int n) {
        compiler.setFont("Gothic5");
        String digits = String.format("%d", n);
        if( digits.length() == 7  || digits.length() == 5 ){
            digits = "0" + digits;
        }
        printDigits(digits, hokenshabangouBoxes);
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

    public void putKouhiFutanshaBangou1(int n) {
        compiler.setFont("Gothic5");
        printDigits(n, kouhifutanshabangou1);
    }

    public void putKouhiJukyuushaBangou1(int n) {
        compiler.setFont("Gothic5");
        printDigits(n, kouhijukyuushabangou1);
    }

    public void putKouhiFutanshaBangou2(int n) {
        compiler.setFont("Gothic5");
        printDigits(n, kouhifutanshabangou2);
    }

    public void putKouhiJukyuushaBangou2(int n) {
        compiler.setFont("Gothic5");
        printDigits(n, kouhijukyuushabangou2);
    }

    private void printDigits(String digits, Box[] boxes){
        if (digits.length() > boxes.length) {
            throw new RuntimeException("Too large number: " + digits);
        }
        int offset = boxes.length - digits.length();
        for (int i = 0; i < digits.length(); i++) {
            String c = digits.substring(i, i + 1);
            compiler.textIn(c, boxes[offset + i], HAlign.Center, VAlign.Center);
        }
    }

    private void printDigits(int n, Box[] boxes) {
        String s = String.format("%d", n);
        printDigits(s, boxes);
//        String s = String.format("%d", n);
//        if (s.length() > boxes.length) {
//            throw new RuntimeException("Too large number: " + n);
//        }
//        int offset = boxes.length - s.length();
//        for (int i = 0; i < s.length(); i++) {
//            String c = s.substring(i, i + 1);
//            compiler.textIn(c, boxes[offset + i], HAlign.Center, VAlign.Center);
//        }
    }

    private void setupPatientInfo() {
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

    private void setupPatientInfo_Names(Box box) {
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

    private void setupPatientInfo_Shokumujou(Box box) {
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

    private void setupPatientInfo_Tokkijikou(Box box) {
        Box[] rows = box.splitToRows(4);
        compiler.frameBottom(rows[0]);
        compiler.setFont("Mincho2");
        compiler.textInJustified("特記事項", rows[0].inset(3.5, 0), VAlign.Center);
        this.tokkijikou = rows[1];
    }

    private void doPutShimei(String s) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic4");
            compiler.textIn(s, shimei.displaceLeftEdge(3), HAlign.Left, VAlign.Center);
        });
    }

    public void putShimei(String s) {
        dataValues.shimei = s;
        doPutShimei(s);
    }

    public void markSeibetsuOtoko() {
        identityProcs.add(() -> {
            markCircle(seibetsuOtoko);
        });
    }

    public void markSeibetsuOnna() {
        identityProcs.add(() -> {
            markCircle(seibetsuOnna);
        });
    }

    public void markSeinengappiMeiji() {
        identityProcs.add(() -> {
            markCircle(seinengappiMeiji);
        });
    }

    public void markSeinengappiTaishou() {
        identityProcs.add(() -> {
            markCircle(seinengappiTaishou);
        });
    }

    public void markSeinengappiShouwa() {
        identityProcs.add(() -> {
            markCircle(seinengappiShouwa);
        });
    }

    public void markSeinengappiHeisei() {
        identityProcs.add(() -> {
            markCircle(seinengappiHeisei);
        });
    }

    public void putSeinengappiNen(int nen) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic2");
            compiler.textIn("" + nen, seinengappiNen.displaceRightEdge(-1),
                    HAlign.Right, VAlign.Bottom);
        });
    }

    public void putSeinengappiMonth(int month) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic2");
            compiler.textIn("" + month, seinengappiMonth.displaceRightEdge(-1),
                    HAlign.Right, VAlign.Bottom);
        });
    }

    public void putSeinengappiDay(int day) {
        identityProcs.add(() -> {
            compiler.setFont("Gothic2");
            compiler.textIn("" + day, seinengappiDay.displaceRightEdge(-1),
                    HAlign.Right, VAlign.Bottom);
        });
    }

    public void markShokumujouShokumujou() {
        markCircle(shokumujouShokumujou);
    }

    public void markShokumujouGesen() {
        markCircle(shokumujouGesen);
    }

    public void markShokumujouTsuukin() {
        markCircle(shokumujouTsuukin);
    }

    public void putTokkijikou(String text) {
        compiler.setFont("Mincho2.5");
        compiler.paragraph(text, tokkijikou.inset(1, 1), HAlign.Left, VAlign.Top, 0);
    }

    private Box setBottomCenterAt(Box box, double x, double width) {
        double c = box.getLeft() + x;
        return new Box(c - width / 2, box.getTop(), c + width / 2, box.getBottom());
    }

    private void markLeft(Box box, double width, Consumer<Box> cb) {
        cb.accept(box.flipLeft().setWidth(width, Box.HorizAnchor.Right));
    }

    private void makeIndexAndLabel(Box box, double indexCenter, double indexWidth, double offset,
                                   double labelWidth, BiConsumer<Box, Box> cb) {
        Box rIndex = setBottomCenterAt(box, indexCenter, indexWidth);
        Box rLabel = box.shiftToRight(indexCenter + offset).setWidth(labelWidth, Box.HorizAnchor.Left);
        cb.accept(rIndex, rLabel);
    }

    private void setupIryouKikanLabel() {
        Box box = new Box(110, 57, 120, 73.5);
        Box[] rows = box.splitToVerticallyJustifiedRows(2, 5);
        compiler.setFont("Mincho2");
        compiler.textInJustified("保険医", rows[0], VAlign.Top);
        compiler.textInJustified("療機関", rows[1], VAlign.Top);
        compiler.textInJustified("の所在", rows[2], VAlign.Top);
        compiler.textInJustified("地及び", rows[3], VAlign.Top);
        compiler.textInJustified("名称", rows[4], VAlign.Top);
    }

    private void setupShozaichiMeishou() {
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

    public void putShozaichiMeishouLine1(String s) {
        identityProcs.add(() -> {
            compiler.setFont("Mincho3.5");
            compiler.textIn(s, shozaichimeishouLine1, HAlign.Left, VAlign.Top);
        });
    }

    public void putShozaichiMeishouLine2(String s) {
        identityProcs.add(() -> {
            compiler.setFont("Mincho3.5");
            compiler.textIn(s, shozaichimeishouLine2.displaceLeftEdge(28), HAlign.Left, VAlign.Top);
        });
    }

    public void putShozaichiMeishouLine3(String s) {
        identityProcs.add(() -> {
            compiler.setFont("Mincho4.5");
            compiler.textIn(s, shozaichimeishouLine3.displaceLeftEdge(7), HAlign.Left, VAlign.Top);
        });
    }

    private void setupRcptBody() {
        Box box = new Box(17, 76.5, 199, 263);
        compiler.box(box);
        Box[] rows = box.splitToRows(21, 159);
        setupRcptBodyRow1(rows[0]);
        setupRcptBodyRow2(rows[1]);
        setupRcptBodyRow3(rows[2]);
    }

    private void setupRcptBodyRow1(Box box) {
        compiler.frameBottom(box);
        Box[] cols = box.splitToColumns(100, 136, 160);
        setupRcptBodyRow1_Shoubyoumei(cols[0]);
        setupRcptBodyRow1_ShinryouKaishi(cols[1]);
        setupRcptBodyRow1_Tenki(cols[2]);
        setupRcptBodyRow1_Nissuu(cols[3]);
    }

    private void setupRcptBodyRow1_Shoubyoumei(Box box) {
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
        for (int i = 0; i < 4; i++) {
            Box r = rows[i];
            Box[] rr = r.splitToColumns(4);
            shoubyoumeiNumbers[i] = rr[0];
            shoubyoumeiTexts[i] = rr[1];
        }
        for (int i = 1; i <= 3; i++) {
            renderShoubyoumeiIndex(i);
        }
        shoubyoumeiContinuesToTekiyou = shoubyoumeiTexts[3].shiftUp(2);
        zokushiPresent = shoubyoumeiContinuesToTekiyou.flipDown().shiftDown(1);
    }

    private void renderShoubyoumeiIndex(int index) {
        if (index >= 1 && index <= 4) {
            String label = String.format("(%d)", index);
            compiler.setFont("Mincho2.3");
            compiler.textIn(label, shoubyoumeiNumbers[index - 1], HAlign.Left, VAlign.Bottom);
        }
    }

    // index should be: 1<=index && index <= 4
    public void putShoubyoumei(int index, String s) {
        if (index >= 1 && index <= 4) {
            if (index == 4) {
                renderShoubyoumeiIndex(index);
            }
            compiler.setFont("Gothic3");
            compiler.textIn(s, shoubyoumeiTexts[index - 1], HAlign.Left, VAlign.Center);
        } else {
            System.err.println("Invalid arg for putShoubyoumei: " + index);
        }
    }

    public void putShoubyoumeiEtra(int index, String name, int nen, int month, int day) {
        if (index == 5) {
            compiler.setFont("Gothic2.5");
            compiler.textIn("以下、摘要欄に続く", shoubyoumeiContinuesToTekiyou, HAlign.Right, VAlign.Center);
        }
        String text = String.format("(%d) %s", index, name);
        //extraShoubyoumeiList.add(new TekiyouLine(text));
        String date = String.format("診療開始日　平成%d年%d月%d日", nen, month, day);
        //extraShoubyoumeiList.add(new TekiyouLine(date).setAlignRight());
        extraShoubyoumeiList.add(new TekiyouByoumei(text, date));
    }

    private void setupRcptBodyRow1_ShinryouKaishi(Box box) {
        compiler.frameRight(box);
        Box[] cols = box.splitToColumns(2.5);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("診療開始日", cols[0].inset(0, 3), HAlign.Center);
        Box[] rows = cols[1].inset(2, 3, 1, 3).splitToVerticallyJustifiedRows(2.3, 4);
        compiler.setFont("Mincho2.3");
        for (int i = 0; i < 4; i++) {
            Box[] cc = rows[i].splitToHorizontallyJustifiedColumns(2.3, 4);
            final int index = i;
            shoubyoumeiKaishiNumbers[index] = cc[0];
            shoubyoumeiKaishiNenLabels[index] = cc[1];
            shoubyoumeiKaishiMonthLabels[index] = cc[2];
            shoubyoumeiKaishiDayLabels[index] = cc[3];
            markLeft(cc[1], 6.5, b -> shoubyoumeiKaishiNen[index] = b);
            markLeft(cc[2], 6.5, b -> shoubyoumeiKaishiMonths[index] = b);
            markLeft(cc[3], 6.5, b -> shoubyoumeiKaishiDays[index] = b);
        }
        for (int i = 1; i <= 3; i++) {
            putShoubyoumeiKaishiIndex(i);
            renderShoubyoumeiKaishi(i);
        }
    }

    private void renderShoubyoumeiKaishi(int index) {
        compiler.setFont("Mincho2.3");
        compiler.textIn("年", shoubyoumeiKaishiNenLabels[index - 1], HAlign.Left, VAlign.Bottom);
        compiler.textIn("月", shoubyoumeiKaishiMonthLabels[index - 1], HAlign.Left, VAlign.Bottom);
        compiler.textIn("日", shoubyoumeiKaishiDayLabels[index - 1], HAlign.Left, VAlign.Bottom);
    }

    private void putShoubyoumeiKaishiIndex(int index) {
        if (index >= 1 && index <= 4) {
            compiler.setFont("Mincho2.3");
            String label = String.format("(%d)", index);
            compiler.textIn(label, shoubyoumeiKaishiNumbers[index - 1], HAlign.Left, VAlign.Bottom);
        } else {
            throw new RuntimeException("Invalid shoubyoumei kaishi index: " + index);
        }
    }

    public void putShoubyoumeiKaishiNen(int index, int nen) {
        if (index >= 1 && index <= 4) {
            if (index == 4) {
                putShoubyoumeiKaishiIndex(index);
                renderShoubyoumeiKaishi(index);
            }
            compiler.setFont("Gothic3");
            compiler.textIn("" + nen, shoubyoumeiKaishiNen[index - 1], HAlign.Right, VAlign.Center);
        } else {
            throw new RuntimeException("Invalid shinryou kaishi index: " + index);
        }
    }

    public void putShoubyoumeiKaishiMonth(int index, int month) {
        if (index >= 1 && index <= 4) {
            compiler.setFont("Gothic3");
            compiler.textIn("" + month, shoubyoumeiKaishiMonths[index - 1], HAlign.Right, VAlign.Center);
        } else {
            throw new RuntimeException("Invalid shinryou kaishi index: " + index);
        }
    }

    public void putShoubyoumeiKaishiDay(int index, int day) {
        if (index >= 1 && index <= 4) {
            compiler.setFont("Gothic3");
            compiler.textIn("" + day, shoubyoumeiKaishiDays[index - 1], HAlign.Right, VAlign.Center);
        } else {
            throw new RuntimeException("Invalid shinryou kaishi index: " + index);
        }
    }

    private void setupRcptBodyRow1_Tenki(Box box) {
        compiler.frameRight(box);
        Box[] cols = box.splitToColumns(2.5);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("転帰", cols[0].inset(0, 3), HAlign.Center);
        Box[] cc = cols[1].splitToEvenColumns(3);
        compiler.setPen("dot");
        compiler.frameRight(cc[0]);
        compiler.frameRight(cc[1]);
        compiler.setPen("regular");
        cc = cols[1].shrinkHeight(3, VertAnchor.Bottom).splitToEvenColumns(3);
        {
            Box[] rr = cc[0].splitToRows(2);
            compiler.textIn("治ゆ", rr[0], HAlign.Center, VAlign.Center);
            this.tenkiChiyuMidashi = rr[0].getCenterPoint();
            this.tenkiChiyu = rr[1].shrinkHeight(2, VertAnchor.Bottom);
        }
        {
            Box[] rr = cc[1].splitToRows(2);
            compiler.textIn("死亡", rr[0], HAlign.Center, VAlign.Center);
            this.tenkiShibouMidashi = rr[0].getCenterPoint();
            this.tenkiShibou = rr[1].shrinkHeight(2, VertAnchor.Bottom);
        }
        {
            Box[] rr = cc[2].splitToRows(2);
            compiler.textIn("中止", rr[0], HAlign.Center, VAlign.Center);
            this.tenkiChuushiMidashi = rr[0].getCenterPoint();
            this.tenkiChuushi = rr[1].shrinkHeight(2, VertAnchor.Bottom);
        }
    }

    private static Pattern tenkiPattern = Pattern.compile("(\\d+)(?:\\((.+)\\))?");

    private String[] parseTenki(String s) {
        Matcher matcher = tenkiPattern.matcher(s.trim());
        if (matcher.matches()) {
            return new String[]{matcher.group(1), matcher.group(2)};
        } else {
            throw new RuntimeException("Invalid chiyu: " + s);
        }
    }

    private int tenkiCount = 0;

    public void putChiyu(String[] parts) {
        markCircle(tenkiChiyuMidashi);
        compiler.setFont("Mincho2.3");
        for (String part : parts) {
            String[] toks = parseTenki(part);
            String text = String.format("(%s)", toks[0]);
            if (toks.length > 1) {
                text += " " + toks[1];
            }
            Box box = tenkiChiyu.shrinkHeight(tenkiCount * 2.3, VertAnchor.Bottom).inset(2, 0);
            compiler.textIn(text, box, HAlign.Left, VAlign.Top);
            tenkiCount += 1;
        }
    }

    public void putChuushi(String[] parts) {
        markCircle(tenkiChuushiMidashi);
        compiler.setFont("Mincho2.3");
        for (String part : parts) {
            String[] toks = parseTenki(part);
            String text = String.format("(%s)", toks[0]);
            if (toks.length > 1) {
                text = toks[1] + " " + text;
            }
            Box box = tenkiChuushi.shrinkHeight(tenkiCount * 2.3, VertAnchor.Bottom).inset(2, 0);
            compiler.textIn(text, box, HAlign.Right, VAlign.Top);
            tenkiCount += 1;
        }
    }

    public void putShibou(String[] parts) {
        markCircle(tenkiShibouMidashi);
        compiler.setFont("Mincho2.3");
        if (parts.length > 0) {
            String part = parts[0];
            String[] toks = parseTenki(part);
            if (toks.length > 1) {
                String text = String.format("%s 死亡", toks[1]);
                Box box = tenkiShibou.shrinkHeight(tenkiCount * 2.3, VertAnchor.Bottom).inset(2, 0);
                compiler.textIn(text, box, HAlign.Center, VAlign.Top);
            }
            tenkiCount += 1;
        }
    }

    private void setupRcptBodyRow1_Nissuu(Box box) {
        compiler.setPen("bold");
        compiler.box(box);
        compiler.setPen("regular");
        Box[] cols = box.splitToColumns(2.5);
        compiler.frameRight(cols[0]);
        compiler.setFont("Mincho2");
        compiler.textInVertJustified("診療実日数", cols[0].inset(0, 3), HAlign.Center);
        Box[] rows = cols[1].splitToEvenRows(3);
        {
            Box r = rows[0];
            compiler.frameBottom(r);
            Box[] cc = r.splitToColumns(2.5);
            compiler.frameRight(cc[0]);
            compiler.textInVertJustified("保健", cc[0].inset(0, 1), HAlign.Center);
            Box q = cc[1].displaceRightEdge(-2).setHeight(2.5, VertAnchor.Center);
            compiler.textIn("日", q, HAlign.Right, VAlign.Center);
            this.shinryounissuHoken = q.displaceRightEdge(-4);
        }
        {
            Box r = rows[1];
            compiler.frameBottom(r);
            Box[] cc = r.splitToColumns(2.5);
            compiler.frameRight(cc[0]);
            compiler.textInVertJustified("公費①", cc[0].inset(0, 1), HAlign.Center);
            Box q = cc[1].displaceRightEdge(-2).setHeight(2.5, VertAnchor.Center);
            compiler.textIn("日", q, HAlign.Right, VAlign.Center);
            this.shinryounissuKouhi1 = q.displaceRightEdge(-4);
        }
        {
            Box r = rows[2];
            compiler.frameBottom(r);
            Box[] cc = r.splitToColumns(2.5);
            compiler.frameRight(cc[0]);
            compiler.textInVertJustified("公費②", cc[0].inset(0, 1), HAlign.Center);
            Box q = cc[1].displaceRightEdge(-2).setHeight(2.5, VertAnchor.Center);
            compiler.textIn("日", q, HAlign.Right, VAlign.Center);
            this.shinryounissuKouhi2 = q.displaceRightEdge(-4);
        }
    }

    public void putShinryouNissuuHoken(int days) {
        compiler.setFont("Gothic2.5");
        compiler.textIn("" + days, shinryounissuHoken, HAlign.Right, VAlign.Center);
    }

    public void putShinryouNissuuKouhi1(int days) {
        compiler.setFont("Gothic2.5");
        compiler.textIn("" + days, shinryounissuKouhi1, HAlign.Right, VAlign.Center);
    }

    public void putShinryouNissuuKouhi2(int days) {
        compiler.setFont("Gothic2.5");
        compiler.textIn("" + days, shinryounissuKouhi2, HAlign.Right, VAlign.Center);
    }

    private void setupRcptBodyRow2(Box box) {
        Box[] cols = box.splitToColumns(90);
        Box r1 = cols[0];
        Box r2 = cols[1];
        {
            Box p;
            Box q;
            compiler.frameRight(r1);
            {
                Box[] cc = r1.splitToColumns(71);
                p = cc[0];
                q = cc[1];
            }
            compiler.frameRight(p);
            Box[] rr = p.splitToRows(4, 24, 28, 51, 84.5, 96.5);
            setupRcptBodyRow2_Shoshin(rr[0]);
            setupRcptBodyRow2_Saishin(rr[1]);
            setupRcptBodyRow2_Shidou(rr[2]);
            setupRcptBodyRow2_Zaitaku(rr[3]);
            setupRcptBodyRow2_Touyaku(rr[4]);
            setupRcptBodyRow2_Chuusha(rr[5]);
            drawBottomDotLine(q, Arrays.copyOfRange(rr, 1, rr.length - 1));
            {
                Box[] ss = rr[6].splitToEvenRows(5);
                setupRcptBodyRow2_Shochi(ss[0]);
                setupRcptBodyRow2_Shujutsu(ss[1]);
                setupRcptBodyRow2_Kensa(ss[2]);
                setupRcptBodyRow2_Gazou(ss[3]);
                setupRcptBodyRow2_Sonota(ss[4]);
                drawBottomDotLine(q, Arrays.copyOfRange(ss, 0, ss.length - 1));
            }
            q = q.inset(3.5, 1, 3.5, 0);
            compiler.setFont("Mincho1.5");
            compiler.textInJustified("公費分点数", q, VAlign.Top);
        }
        {
            Box p;
            {
                Box[] cc = r2.splitToColumns(tekiyouLeftColumnWidth);
                p = cc[0];
            }
            compiler.setPen("dot");
            compiler.frameRight(p);
            compiler.setPen("regular");
            this.tekiyou = r2;
        }
    }

    private void drawBottomDotLine(Box q, Box[] boxes) {
        double left = q.getLeft();
        double right = q.getRight();
        compiler.setPen("dot");
        for (Box box : boxes) {
            double bottom = box.getBottom();
            compiler.line(left, bottom, right, bottom);
        }
        compiler.setPen("regular");
    }

    private void setupRcptBodyRow2_Shoshin(Box box) {
        Box r1, r2;

        compiler.setFont("Mincho2.5");
        compiler.frameBottom(box);
        {
            Box[] cc = box.splitToColumns(21);
            r1 = cc[0];
            r2 = cc[1];
        }
        compiler.frameRight(r1);
        {
            Box p, q;
            double x[] = {32, 46.5};
            Box[] cc;
            {
                Box[] tmp = r1.splitToColumns(5);
                p = tmp[0];
                q = tmp[1];
            }
            compiler.textIn("11", p, HAlign.Center, VAlign.Center);
            q = q.inset(2, 0, 4.5, 0);
            compiler.textInJustified("初診", q, VAlign.Center);
            cc = r2.splitToColumns(x);
            cc[0] = cc[0].displaceLeftEdge(1);
            compiler.textIn("時間外・休日・深夜", cc[0], HAlign.Left, VAlign.Center);
            {
                Box r = cc[0];
                double f = compiler.getCurrentFontSize();
                shoshinJikangai = r.setWidth(f * 3, HorizAnchor.Left).getCenterPoint();
                shoshinKyuujitsu = r.shiftToRight(f * 4).setWidth(f * 2, HorizAnchor.Left).getCenterPoint();
                shoshinShinya = r.shiftToRight(f * 7).setWidth(f * 2, HorizAnchor.Left).getCenterPoint();
            }
            renderKaiTen(r2, "回", boxes -> {
                shoshinKai = boxes[0];
                shoshinTen = boxes[1];
            });
            compiler.textIn("点", r2.shiftToLeft(1), HAlign.Right, VAlign.Center);
        }
    }

    private void renderTen(Box box, Consumer<Box> cb) {
        cb.accept(box.shiftToLeft(5).setWidth(9, HorizAnchor.Right));
    }

    private void renderKaiTen(Box box, String kaiLabel, Consumer<Box[]> cb) {
        Box kaiBox = box.setWidth(18, HorizAnchor.Right);
        compiler.textIn(kaiLabel, kaiBox, HAlign.Left, VAlign.Center);
        Box kaiInput = kaiBox.flipLeft().shiftToLeft(1).setWidth(6, HorizAnchor.Right);
        renderTen(box, tenInput -> {
            cb.accept(new Box[]{kaiInput, tenInput});
        });
    }

    private void renderTankaKaiTen(Box box, String kaiLabel, Consumer<Box[]> cb) {
        Box tankaBox = box.setWidth(29, HorizAnchor.Right);
        compiler.textIn("×", tankaBox, HAlign.Left, VAlign.Center);
        Box tankaInput = tankaBox.flipLeft().shiftToLeft(1).setWidth(6, HorizAnchor.Right);
        renderKaiTen(box, kaiLabel, boxes -> {
            cb.accept(new Box[]{tankaInput, boxes[0], boxes[1]});
        });
    }

    public void markShoshinJikangai() {
        markCircle(shoshinJikangai);
    }

    public void markShoshinKyuujitsu() {
        markCircle(shoshinKyuujitsu);
    }

    public void markShoshinShinya() {
        markCircle(shoshinShinya);
    }

    private void putTankaKaiTen(Box box, int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, box, HAlign.Right, VAlign.Center);
    }

    public void putShoshinTimes(int n) {
        putTankaKaiTen(shoshinKai, n);
    }

    public void putShoshinTen(int n) {
        putTankaKaiTen(shoshinTen, n);
    }

    private void setupRcptBodyRow2_Saishin(Box box) {
        Box r1, r2;

        compiler.frameBottom(box);
        {
            Box[] tmp = box.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(5.5);
                p = tmp[0];
                q = tmp[1];
            }
            q = q.inset(0, 3);
            compiler.textIn("12", p, HAlign.Center, VAlign.Bottom);
            compiler.textInVertJustified("再診", q, HAlign.Center);
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(5);
            RcptBodyRow2_Format1(rr[0], "再診", boxes -> {
                saishinSaishinTanka = boxes[0];
                saishinSaishinTimes = boxes[1];
                saishinSaishinTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[1], "外来管理加算", boxes -> {
                saishinGairaiKanriTanka = boxes[0];
                saishinGairaiKanriTimes = boxes[1];
                saishinGairaiKanriTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[2], "時間外", boxes -> {
                saishinJikangaiTanka = boxes[0];
                saishinJikangaiTimes = boxes[1];
                saishinJikangaiTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[3], "休日", boxes -> {
                saishinKyuujitsuTanka = boxes[0];
                saishinKyuujitsuTimes = boxes[1];
                saishinKyuujitsuTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[4], "深夜", boxes -> {
                saishinShinyaTanka = boxes[0];
                saishinShinyaTimes = boxes[1];
                saishinShinyaTen = boxes[2];
            });
        }
    }

    private void RcptBodyRow2_Format1(Box box, String label, Consumer<Box[]> cb) {
        compiler.textInJustified(label, box.setWidth(17, HorizAnchor.Left).displaceLeftEdge(2), VAlign.Center);
        renderTankaKaiTen(box, "回", cb);
    }

    private void RcptBodyRow2_Format2(Box box, String label, Consumer<Box[]> cb) {
        compiler.textInJustified(label, box.setWidth(21, HorizAnchor.Left).displaceLeftEdge(2), VAlign.Center);
        renderTankaKaiTen(box, "回", cb);
    }

    private void RcptBodyRow2_Format2_tt(Box box, String label, Consumer<Box[]> cb) {
        compiler.textInJustified(label, box.setWidth(21, HorizAnchor.Left).displaceLeftEdge(2), VAlign.Center);
        renderKaiTen(box, "回", cb);
    }

    private void RcptBodyRow2_Format3(Box box, String label, Consumer<Box> cb) {
        compiler.textInJustified(label, box.setWidth(17, HorizAnchor.Left).displaceLeftEdge(2), VAlign.Center);
        renderTen(box, cb);
    }

    public void putSaishinSaishinTanka(int n) {
        putTankaKaiTen(saishinSaishinTanka, n);
    }

    public void putSaishinSaishinTimes(int n) {
        putTankaKaiTen(saishinSaishinTimes, n);
    }

    public void putSaishinSaishinTen(int n) {
        putTankaKaiTen(saishinSaishinTen, n);
    }

    public void putSaishinGairaiKanriTanka(int n) {
        putTankaKaiTen(saishinGairaiKanriTanka, n);
    }

    public void putSaishinGairaiKanriTimes(int n) {
        putTankaKaiTen(saishinGairaiKanriTimes, n);
    }

    public void putSaishinGairaiKanriTen(int n) {
        putTankaKaiTen(saishinGairaiKanriTen, n);
    }

    public void putSaishinJikangaiTanka(int n) {
        putTankaKaiTen(saishinJikangaiTanka, n);
    }

    public void putSaishinJikangaiTimes(int n) {
        putTankaKaiTen(saishinJikangaiTimes, n);
    }

    public void putSaishinJikangaiTen(int n) {
        putTankaKaiTen(saishinJikangaiTen, n);
    }

    public void putSaishinKyuujitsuTanka(int n) {
        putTankaKaiTen(saishinKyuujitsuTanka, n);
    }

    public void putSaishinKyuujitsuTimes(int n) {
        putTankaKaiTen(saishinKyuujitsuTimes, n);
    }

    public void putSaishinKyuujitsuTen(int n) {
        putTankaKaiTen(saishinKyuujitsuTen, n);
    }

    public void putSaishinShinyaTanka(int n) {
        putTankaKaiTen(saishinShinyaTanka, n);
    }

    public void putSaishinShinyaTimes(int n) {
        putTankaKaiTen(saishinShinyaTimes, n);
    }

    public void putSaishinShinyaTen(int n) {
        putTankaKaiTen(saishinShinyaTen, n);
    }

    private void setupRcptBodyRow2_Shidou(Box r) {
        Box r1;

        compiler.frameBottom(r);
        compiler.setFont("Mincho2.5");
        {
            Box[] tmp = r.splitToColumns(21);
            r1 = tmp[0];
        }
        compiler.frameRight(r1);
        {
            Box p, q;
            {
                Box[] tmp = r1.splitToColumns(5);
                p = tmp[0];
                q = tmp[1];
            }
            compiler.textIn("13", p, HAlign.Center, VAlign.Center);
            q = q.inset(2, 0, 4.5, 0);
            compiler.textInJustified("指導", q, VAlign.Center);
            renderTen(r, tenBox -> this.shidouTen = tenBox);
        }
    }

    public void putShidouTen(int ten) {
        putTankaKaiTen(shidouTen, ten);
    }

    private void setupRcptBodyRow2_Zaitaku(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(5.5);
                p = tmp[0];
                q = tmp[1];
            }
            q = q.inset(0, 3);
            compiler.textIn("14", p, HAlign.Center, VAlign.Bottom);
            compiler.textInVertJustified("在宅", q, HAlign.Center);
        }
        {
            Box[] rr = r2.splitToEvenRows(6);
            RcptBodyRow2_Format1(rr[0], "往診", boxes -> {
                this.zaitakuOushinTanka = boxes[0];
                this.zaitakuOushinTimes = boxes[1];
                this.zaitakuOushinTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[1], "夜間", boxes -> {
                this.zaitakuYakanTanka = boxes[0];
                this.zaitakuYakanTimes = boxes[1];
                this.zaitakuYakanTen = boxes[2];
            });
            RcptBodyRow2_Format1(rr[2], "深夜・緊急", boxes -> {
                this.zaitakuShinyaTanka = boxes[0];
                this.zaitakuShinyaTimes = boxes[1];
                this.zaitakuShinyaTen = boxes[2];
            });
            RcptBodyRow2_Format2(rr[3], "在宅患者訪問診療", boxes -> {
                this.zaitakuZaitakuTanka = boxes[0];
                this.zaitakuZaitakuTimes = boxes[1];
                this.zaitakuZaitakuTen = boxes[2];
            });
            RcptBodyRow2_Format3(rr[4], "その他", box -> this.zaitakuSonotaTen = box);
            RcptBodyRow2_Format3(rr[5], "薬剤", box -> this.zaitakuYakuzaiTen = box);
        }
    }

    public void putZaitakuOushinTanka(int n) {
        putTankaKaiTen(zaitakuOushinTanka, n);
    }

    public void putZaitakuOushinTimes(int n) {
        putTankaKaiTen(zaitakuOushinTimes, n);
    }

    public void putZaitakuOushinTen(int n) {
        putTankaKaiTen(zaitakuOushinTen, n);
    }

    public void putZaitakuYakanTanka(int n) {
        putTankaKaiTen(zaitakuYakanTanka, n);
    }

    public void putZaitakuYakanTimes(int n) {
        putTankaKaiTen(zaitakuYakanTimes, n);
    }

    public void putZaitakuYakanTen(int n) {
        putTankaKaiTen(zaitakuYakanTen, n);
    }

    public void putZaitakuShinyaTanka(int n) {
        putTankaKaiTen(zaitakuShinyaTanka, n);
    }

    public void putZaitakuShinyaTimes(int n) {
        putTankaKaiTen(zaitakuShinyaTimes, n);
    }

    public void putZaitakuShinyaTen(int n) {
        putTankaKaiTen(zaitakuShinyaTen, n);
    }

    public void putZaitakuZaitakuTanka(int n) {
        putTankaKaiTen(zaitakuZaitakuTanka, n);
    }

    public void putZaitakuZaitakuTimes(int n) {
        putTankaKaiTen(zaitakuZaitakuTimes, n);
    }

    public void putZaitakuZaitakuTen(int n) {
        putTankaKaiTen(zaitakuZaitakuTen, n);
    }

    public void putZaitakuSonotaTen(int n) {
        putTankaKaiTen(zaitakuSonotaTen, n);
    }

    public void putZaitakuYakuzaiTen(int n) {
        putTankaKaiTen(zaitakuYakuzaiTen, n);
    }

    private void setupRcptBodyRow2_Touyaku(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(7);
                p = tmp[0];
                q = tmp[1];
            }
            q = q.inset(0, 3);
            compiler.textIn("20", p, HAlign.Center, VAlign.Bottom);
            compiler.textInVertJustified("投薬", q, HAlign.Center);
        }
        {
            double x[] = {7.5, 13, 20};
            Box[] rr, ss, tt;
            Box b;

            rr = r2.splitToRows(x);
            ss = rr[3].splitToEvenRows(3);
            RcptBodyRow2_Format4(rr[0], "21", "内服");
            RcptBodyRow2_Format5(rr[1], "22", "頓服");
            RcptBodyRow2_Format4(rr[2], "23", "外用");
            RcptBodyRow2_Format6(ss[0], "25", "処方");
            RcptBodyRow2_Format6(ss[1], "26", "麻毒");
            RcptBodyRow2_Format6(ss[2], "27", "調基");

            tt = rr[0].splitToEvenRows(2);
            renderKaiTen(tt[0], "単位", boxes -> {
                this.touyakuNaifukuYakuzaiTimes = boxes[0];
                this.touyakuNaifukuYakuzaiTen = boxes[1];
            });
            renderTankaKaiTen(tt[1], "回", boxes -> {
                this.touyakuNaifukuChouzaiTanka = boxes[0];
                this.touyakuNaifukuChouzaiTimes = boxes[1];
                this.touyakuNaifukuChouzaiTen = boxes[2];
            });
            renderKaiTen(rr[1], "単位", boxes -> {
                this.touyakuTonpukuYakuzaiTimes = boxes[0];
                this.touyakuTonpukuYakuzaiTen = boxes[1];
            });
            tt = rr[2].splitToEvenRows(2);
            renderKaiTen(tt[0], "単位", boxes -> {
                this.touyakuGaiyouYakuzaiTimes = boxes[0];
                this.touyakuGaiyouYakuzaiTen = boxes[1];
            });
            renderTankaKaiTen(tt[1], "回", boxes -> {
                this.touyakuGaiyouChouzaiTanka = boxes[0];
                this.touyakuGaiyouChouzaiTimes = boxes[1];
                this.touyakuGaiyouChouzaiTen = boxes[2];
            });
            renderTankaKaiTen(ss[0], "回", boxes -> {
                this.touyakuShohouTanka = boxes[0];
                this.touyakuShohouTimes = boxes[1];
                this.touyakuShohouTen = boxes[2];
            });
            renderKaiTen(ss[1], "回", boxes -> {
                this.touyakuMadokuTimes = boxes[0];
                this.touyakuMadokuTen = boxes[1];
            });
            renderTen(ss[2], box -> {
                this.touyakuChoukiTen = box;
            });
        }
    }

    public void putTouyakuNaifukuYakuzaiTimes(int n) {
        putTankaKaiTen(touyakuNaifukuYakuzaiTimes, n);
    }

    public void putTouyakuNaifukuYakuzaiTen(int n) {
        putTankaKaiTen(touyakuNaifukuYakuzaiTen, n);
    }

    public void putTouyakuNaifukuChouzaiTanka(int n) {
        putTankaKaiTen(touyakuNaifukuChouzaiTanka, n);
    }

    public void putTouyakuNaifukuChouzaiTimes(int n) {
        putTankaKaiTen(touyakuNaifukuChouzaiTimes, n);
    }

    public void putTouyakuNaifukuChouzaiTen(int n) {
        putTankaKaiTen(touyakuNaifukuChouzaiTen, n);
    }

    public void putTouyakuTonpukuYakuzaiTimes(int n) {
        putTankaKaiTen(touyakuTonpukuYakuzaiTimes, n);
    }

    public void putTouyakuTonpukuYakuzaiTen(int n) {
        putTankaKaiTen(touyakuTonpukuYakuzaiTen, n);
    }

    public void putTouyakuGaiyouYakuzaiTimes(int n) {
        putTankaKaiTen(touyakuGaiyouYakuzaiTimes, n);
    }

    public void putTouyakuGaiyouYakuzaiTen(int n) {
        putTankaKaiTen(touyakuGaiyouYakuzaiTen, n);
    }

    public void putTouyakuGaiyouChouzaiTanka(int n) {
        putTankaKaiTen(touyakuGaiyouChouzaiTanka, n);
    }

    public void putTouyakuGaiyouChouzaiTimes(int n) {
        putTankaKaiTen(touyakuGaiyouChouzaiTimes, n);
    }

    public void putTouyakuGaiyouChouzaiTen(int n) {
        putTankaKaiTen(touyakuGaiyouChouzaiTen, n);
    }

    public void putTouyakuShohouTanka(int n) {
        putTankaKaiTen(touyakuShohouTanka, n);
    }

    public void putTouyakuShohouTimes(int n) {
        putTankaKaiTen(touyakuShohouTimes, n);
    }

    public void putTouyakuShohouTen(int n) {
        putTankaKaiTen(touyakuShohouTen, n);
    }

    public void putTouyakuMadokuTimes(int n) {
        putTankaKaiTen(touyakuMadokuTimes, n);
    }

    public void putTouyakuMadokuTen(int n) {
        putTankaKaiTen(touyakuMadokuTen, n);
    }

    public void putTouyakuChoukiTen(int n) {
        putTankaKaiTen(touyakuChoukiTen, n);
    }

    private void RcptBodyRow2_Format4(Box r, String index, String label) {
        Box p;
        {
            Box[] tmp = r.splitToColumns(21);
            p = tmp[0];
        }
        {
            Box a, b;
            Box[] rr;
            {
                Box[] tmp = p.splitToColumns(10.5);
                a = tmp[0];
                b = tmp[1];
            }
            a = a.inset(1, 0, 1, 0);
            compiler.textIn(index, a, HAlign.Left, VAlign.Center);
            compiler.textIn(label, a, HAlign.Right, VAlign.Center);
            rr = b.splitToEvenRows(2);
            compiler.textInJustified("薬剤", rr[0], VAlign.Center);
            compiler.textInJustified("調剤", rr[1], VAlign.Center);
        }
    }

    private void RcptBodyRow2_Format5(Box r, String index, String label) {
        Box p;
        {
            Box[] tmp = r.splitToColumns(21);
            p = tmp[0];
        }
        {
            Box a, b;
            {
                Box[] tmp = p.splitToColumns(10.5);
                a = tmp[0];
                b = tmp[1];
            }
            a = a.inset(1, 0, 1, 0);
            compiler.textIn(index, a, HAlign.Left, VAlign.Center);
            compiler.textIn(label, a, HAlign.Right, VAlign.Center);
            compiler.textInJustified("薬剤", b, VAlign.Center);
        }
    }

    private void RcptBodyRow2_Format6(Box r, String index, String label) {
        Box p;
        {
            Box[] tmp = r.splitToColumns(21);
            p = tmp[0];
        }
        {
            Box a, b;
            {
                Box[] tmp = p.splitToColumns(4.5);
                a = tmp[0];
                b = tmp[1];
            }
            a = a.inset(1, 0, 1, 0);
            compiler.textIn(index, a, HAlign.Left, VAlign.Center);
            compiler.textInJustified(label, b, VAlign.Center);
        }
    }

    private void setupRcptBodyRow2_Chuusha(Box r) {
        Box r1, r2;
        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];
            }
            q = q.inset(0, 2, 0, 1);
            compiler.textIn("30", p, HAlign.Center, VAlign.Bottom);
            compiler.textInVertJustified("注射", q, HAlign.Center);
        }
        {
            Box[] rr;
            rr = r2.splitToEvenRows(3);
            RcptBodyRow2_Format6(rr[0], "31", "皮下筋肉内");
            RcptBodyRow2_Format6(rr[1], "32", "静脈内");
            RcptBodyRow2_Format6(rr[2], "33", "その他");
            renderKaiTen(rr[0], "回", boxes -> {
                this.chuushaHikaTimes = boxes[0];
                this.chuushaHikaTen = boxes[1];
            });
            renderKaiTen(rr[1], "回", boxes -> {
                this.chuushaJoumyakuTimes = boxes[0];
                this.chuushaJoumyakuTen = boxes[1];
            });
            renderKaiTen(rr[2], "回", boxes -> {
                this.chuushaSonotaTimes = boxes[0];
                this.chuushaSonotaTen = boxes[1];
            });
        }
    }

    public void putChuushaHikaTimes(int n) {
        putTankaKaiTen(chuushaHikaTimes, n);
    }

    public void putChuushaHikaTen(int n) {
        putTankaKaiTen(chuushaHikaTen, n);
    }

    public void putChuushaJoumyakuTimes(int n) {
        putTankaKaiTen(chuushaJoumyakuTimes, n);
    }

    public void putChuushaJoumyakuTen(int n) {
        putTankaKaiTen(chuushaJoumyakuTen, n);
    }

    public void putChuushaSonotaTimes(int n) {
        putTankaKaiTen(chuushaSonotaTimes, n);
    }

    public void putChuushaSonotaTen(int n) {
        putTankaKaiTen(chuushaSonotaTen, n);
    }

    private void setupRcptBodyRow2_Shochi(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];

            }
            compiler.textIn("40", p, HAlign.Center, VAlign.Bottom);
            q = q.inset(0, 0.5);
            compiler.textInVertJustified("処置", q, HAlign.Center);
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(2);
            renderKaiTen(rr[0], "回", boxes -> {
                this.shochiTimes = boxes[0];
                this.shochiTen = boxes[1];
            });
            RcptBodyRow2_Format3(rr[1], "薬剤", box -> this.shochiYakuzaiTen = box);
        }
    }

    public void putShochiTimes(int n) {
        putTankaKaiTen(shochiTimes, n);
    }

    public void putShochiTen(int n) {
        putTankaKaiTen(shochiTen, n);
    }

    public void putShochiYakuzaiTen(int n) {
        putTankaKaiTen(shochiYakuzaiTen, n);
    }

    private void setupRcptBodyRow2_Shujutsu(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];
            }
            compiler.textIn("50", p, HAlign.Center, VAlign.Bottom);
            q = q.inset(0, 0.5);
            Box[] cc = q.splitToEvenColumns(2);
            compiler.setFont("Mincho1.8");
            compiler.textInVertJustified("手術", cc[0], HAlign.Center);
            compiler.textInVertJustified("麻酔", cc[1], HAlign.Center);
            compiler.setFont("Mincho2.5");
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(2);
            renderKaiTen(rr[0], "回", boxes -> {
                this.shujutsuTimes = boxes[0];
                this.shujutsuTen = boxes[1];
            });
            RcptBodyRow2_Format3(rr[1], "薬剤", box -> this.shujutsuYakuzaiTen = box);
        }
    }

    public void putShujutsuTimes(int n) {
        putTankaKaiTen(shujutsuTimes, n);
    }

    public void putShujutsuTen(int n) {
        putTankaKaiTen(shujutsuTen, n);
    }

    public void putShujutsuYakuzaiTen(int n) {
        putTankaKaiTen(shujutsuYakuzaiTen, n);
    }

    private void setupRcptBodyRow2_Kensa(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];

            }
            compiler.textIn("60", p, HAlign.Center, VAlign.Bottom);
            q = q.inset(0, 0.5);
            compiler.textInVertJustified("検査", q, HAlign.Center);
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(2);
            renderKaiTen(rr[0], "回", boxes -> {
                this.kensaTimes = boxes[0];
                this.kensaTen = boxes[1];
            });
            RcptBodyRow2_Format3(rr[1], "薬剤", box -> this.kensaYakuzaiTen = box);
        }
    }

    public void putKensaTimes(int n) {
        putTankaKaiTen(kensaTimes, n);
    }

    public void putKensaTen(int n) {
        putTankaKaiTen(kensaTen, n);
    }

    public void putKensaYakuzaiTen(int n) {
        putTankaKaiTen(kensaYakuzaiTen, n);
    }

    private void setupRcptBodyRow2_Gazou(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];
            }
            compiler.textIn("70", p, HAlign.Center, VAlign.Bottom);
            q = q.inset(0, 0.5);
            Box[] cc = q.splitToEvenColumns(2);
            compiler.setFont("Mincho1.8");
            compiler.textInVertJustified("画像", cc[0], HAlign.Center);
            compiler.textInVertJustified("診断", cc[1], HAlign.Center);
            compiler.setFont("Mincho2.5");
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(2);
            renderKaiTen(rr[0], "回", boxes -> {
                this.gazouTimes = boxes[0];
                this.gazouTen = boxes[1];
            });
            RcptBodyRow2_Format3(rr[1], "薬剤", box -> this.gazouYakuzaiTen = box);
        }
    }

    public void putGazouTimes(int n) {
        putTankaKaiTen(gazouTimes, n);
    }

    public void putGazouTen(int n) {
        putTankaKaiTen(gazouTen, n);
    }

    public void putGazouYakuzaiTen(int n) {
        putTankaKaiTen(gazouYakuzaiTen, n);
    }

    private void setupRcptBodyRow2_Sonota(Box r) {
        Box r1, r2;

        compiler.frameBottom(r);
        {
            Box[] tmp = r.splitToColumns(5);
            r1 = tmp[0];
            r2 = tmp[1];
        }
        compiler.setFont("Mincho2.5");
        {
            Box p, q;

            compiler.frameRight(r1);
            {
                Box[] tmp = r1.splitToRows(3);
                p = tmp[0];
                q = tmp[1];

            }
            compiler.textIn("80", p, HAlign.Center, VAlign.Bottom);
            q = q.inset(0, 0.5);
            compiler.setFont("Mincho1.8");
            compiler.textInVertJustified("その他", q, HAlign.Center);
            compiler.setFont("Mincho2.5");
        }
        {
            Box[] rr;

            rr = r2.splitToEvenRows(3);
            RcptBodyRow2_Format2_tt(rr[0], "処方せん", boxes -> {
                this.sonotaShohousenTimes = boxes[0];
                this.sonotaShohousenTen = boxes[1];
            });
            RcptBodyRow2_Format3(rr[1], "その他", box -> this.sonotaSonotaTen = box);
            RcptBodyRow2_Format3(rr[2], "薬剤", box -> this.sonotaYakuzaiTen = box);
        }
    }

    public void putSonotaShohousenTimes(int n) {
        putTankaKaiTen(sonotaShohousenTimes, n);
    }

    public void putSonotaShohousenTen(int n) {
        putTankaKaiTen(sonotaShohousenTen, n);
    }

    public void putSonotaSonotaTen(int n) {
        putTankaKaiTen(sonotaSonotaTen, n);
    }

    public void putSonotaYakuzaiTen(int n) {
        putTankaKaiTen(sonotaYakuzaiTen, n);
    }

    private void setupRcptBodyRow3(Box r) {
        compiler.setFont("Mincho2.5");
        compiler.frameTop(r);
        Box c1, c2;
        {
            Box[] tmp = r.splitToColumns(114);
            c1 = tmp[0];
            c2 = tmp[1];
        }
        compiler.setPen("bold");
        compiler.box(c1);
        compiler.setPen("regular");
        Box[] cc = c1.splitToColumns(2.5, 5, 50, 90);
        compiler.frameRight(cc[0]);
        compiler.textInVertJustified("療養の給付", cc[0].inset(0, 3), HAlign.Center);
        compiler.frameRight(cc[1]);
        compiler.frameRight(cc[2]);
        compiler.frameRight(cc[3]);
        {
            Box[] rr = cc[1].splitToEvenRows(3);
            compiler.frameBottom(rr[0]);
            compiler.frameBottom(rr[1]);
            compiler.textInVertJustified("保健", rr[0].inset(0, 1.5), HAlign.Center);
            compiler.textInVertJustified("公費①", rr[1], HAlign.Center);
            compiler.textInVertJustified("公費②", rr[2], HAlign.Center);
        }
        setupRcptBodyRow3_c1(cc[2]);
        setupRcptBodyRow3_c2(cc[3]);
        setupRcptBodyRow3_c3(cc[4]);
        setupRcptBodyRow3_c4(c2);
    }

    private void setupRcptBodyRow3_c1(Box r) {
        Box[] rr = r.splitToEvenRows(3);
        compiler.frameBottom(rr[0]);
        compiler.frameBottom(rr[1]);
        {
            Box p, q;
            {
                Box[] tmp = rr[0].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(9, 0, 4, 0);
            compiler.textInJustified("請求点", p, VAlign.Center);
            this.kyuufuHokenSeikyuuten = q.inset(4, 1);
            {
                Box[] tmp = rr[1].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(9, 0, 4, 0);
            compiler.textIn("点", p, HAlign.Right, VAlign.Center);
            this.kyuufuKouhi1Seikyuuten = q.inset(4, 1);
            {
                Box[] tmp = rr[2].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(9, 0, 4, 0);
            compiler.textIn("点", p, HAlign.Right, VAlign.Center);
            this.kyuufuKouhi2Seikyuuten = q.inset(4, 1);
        }
    }

    public void putKyuufuHokenSeikyuuten(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, kyuufuHokenSeikyuuten, HAlign.Right, VAlign.Top);
    }

    public void putKyuufuKouhi1Seikyuuten(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, kyuufuKouhi1Seikyuuten, HAlign.Right, VAlign.Top);
    }

    public void putKyuufuKouhi2Seikyuuten(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, kyuufuKouhi2Seikyuuten, HAlign.Right, VAlign.Top);
    }

    private void setupRcptBodyRow3_c2(Box r) {
        Box[] rr = r.splitToEvenRows(3);
        compiler.frameBottom(rr[0]);
        compiler.frameBottom(rr[1]);
        {
            Box p, a, b;

            {
                Box[] tmp = rr[0].splitToRows(4);
                p = tmp[0];
            }
            {
                {
                    Box[] tmp = p.splitToColumns(6);
                    a = tmp[0];
                    b = tmp[1];
                }
                compiler.textIn("※", a, HAlign.Right, VAlign.Center);
                b = b.inset(6, 0, 4, 0);
                compiler.textInJustified("決定点", b, VAlign.Center);
            }
            {
                Box[] tmp = rr[1].splitToRows(4);
                p = tmp[0];
            }
            {
                {
                    Box[] tmp = p.splitToColumns(6);
                    a = tmp[0];
                    b = tmp[1];
                }
                compiler.textIn("※", a, HAlign.Right, VAlign.Center);
                b = b.inset(6, 0, 4, 0);
                compiler.textIn("点", b, HAlign.Right, VAlign.Center);
            }
            {
                Box[] tmp = rr[2].splitToRows(4);
                p = tmp[0];
            }
            {
                {
                    Box[] tmp = p.splitToColumns(6);
                    a = tmp[0];
                    b = tmp[1];
                }
                compiler.textIn("※", a, HAlign.Right, VAlign.Center);
                b = b.inset(6, 0, 4, 0);
                compiler.textIn("点", b, HAlign.Right, VAlign.Center);
            }
        }
    }

    private void setupRcptBodyRow3_c3(Box r) {
        Box[] rr = r.splitToEvenRows(3);
        compiler.frameBottom(rr[0]);
        compiler.frameBottom(rr[1]);
        {
            Box p, q;
            {
                Box[] tmp = rr[0].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            {
                p = p.inset(1, 0, 1, 0);
                compiler.textInJustified("一部負担金額円", p, VAlign.Center);
                {
                    Box a, b;
                    {
                        Box[] tmp = q.splitToRows(q.getHeight() - 1);
                        a = tmp[0];
                        b = tmp[1];
                    }
                    this.ichibufutankinHoken = a.inset(4, 1);
                    String fontSave = compiler.getCurrentFont();
                    compiler.setFont("Mincho1.1");
                    {
                        Box[] ss;
                        double x[] = {1, 4, 8};

                        ss = b.splitToColumns(x);
                        compiler.textIn("減額", ss[1], HAlign.Left, VAlign.Bottom);
                        compiler.textIn("割(円)免除・支払猶予", ss[3], HAlign.Left, VAlign.Bottom);
                    }
                    compiler.setFont(fontSave);
                }
            }
            {
                Box[] tmp = rr[1].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(1, 0);
            compiler.textIn("円", p, HAlign.Right, VAlign.Center);
            this.ichibufutankinKouhi1 = q.inset(4, 1);
            {
                Box[] tmp = rr[2].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(1, 0);
            compiler.textIn("円", p, HAlign.Right, VAlign.Center);
            this.ichibufutankinKouhi2 = q.inset(4, 1);
        }
    }

    public void putIchibufutankinHoken(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, ichibufutankinHoken, HAlign.Right, VAlign.Top);
    }

    public void putIchibufutankinKouhi1(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, ichibufutankinKouhi1, HAlign.Right, VAlign.Top);
    }

    public void putIchibufutankinKouhi2(int n) {
        compiler.setFont("Gothic3");
        compiler.textIn("" + n, ichibufutankinKouhi2, HAlign.Right, VAlign.Top);
    }

    private void setupRcptBodyRow3_c4(Box r) {
        Box[] rr;

        rr = r.splitToEvenRows(3);
        compiler.frameTop(rr[2]);
        double x[] = {30.5, 49};
        Box[] cc;

        cc = rr[2].splitToColumns(x);
        compiler.frameRight(cc[0]);
        compiler.frameRight(cc[1]);
        {
            Box p, q;
            {
                Box[] tmp = cc[0].splitToRows(4);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(1, 0);
            compiler.textIn("※", p, HAlign.Left, VAlign.Center);
            compiler.textIn("円", p, HAlign.Right, VAlign.Center);
            p = p.inset(3.5, 0, 7, 0);
            compiler.textInJustified("高額療養費", p, VAlign.Center);
        }
        String font = compiler.getCurrentFont();
        compiler.setFont("Mincho1.6");
        {
            Box p, q;
            {
                Box[] tmp = cc[1].splitToRows(3);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(0.5, 0);
            compiler.textIn("※", p, HAlign.Left, VAlign.Center);
            compiler.textIn("点", p, HAlign.Right, VAlign.Center);
            p = p.inset(2.5, 0, 3, 0);
            compiler.textInJustified("公費負担点数", p, VAlign.Center);
        }
        {
            Box p, q;
            {
                Box[] tmp = cc[2].splitToRows(3);
                p = tmp[0];
                q = tmp[1];
            }
            p = p.inset(0.5, 0);
            compiler.textIn("※", p, HAlign.Left, VAlign.Center);
            compiler.textIn("点", p, HAlign.Right, VAlign.Center);
            p = p.inset(2.5, 0, 3, 0);
            compiler.textInJustified("公費負担点数", p, VAlign.Center);
        }
        compiler.setFont(font);
    }

    public void addTekiyou(String index, String body, String tankaTimes) {
        //tekiyouLines.add(new TekiyouLine(index, body, tankaTimes));
        tekiyouLines.add(new TekiyouThreeParts(index, body, tankaTimes));
    }

    public void addTekiyou(Tekiyou tekiyou) {
        //tekiyouLines.add(tekiyouLine);
        tekiyouLines.add(tekiyou);
    }

    public void setDrugBegin(String index, String tanka, String times) {
        if (drugBegin != null) {
            throw new RuntimeException("drugBegin is not null");
        }
        drugBegin = new String[]{index, tanka, times};
    }

    public void addDrug(String label, String amount) {
        tekiyouDrugs.add(String.format("%s %s", label, amount));
    }

    public void flushDrugBegin() {
        String index = drugBegin[0];
        String tanka = drugBegin[1];
        String times = drugBegin[2];
        for (int i = 0; i < tekiyouDrugs.size(); i++) {
            String body = tekiyouDrugs.get(i);
            String indexString = "";
            String tankaTimes = "";
            if (tanka != null) {
                if (index != null && !index.isEmpty()) {
                    indexString = String.format("(%s)", index);
                }
                tankaTimes = String.format("%sx%s", tanka, times);
                index = null;
                tanka = null;
                times = null;
            }
            TekiyouLine tekiyouLine = new TekiyouLine(indexString, body, tankaTimes);
            if (i == 0) {
                tekiyouLine.opts.add(TekiyouLineOpt.GroupBegin);
            }
            if (i == (tekiyouDrugs.size() - 1)) {
                tekiyouLine.opts.add(TekiyouLineOpt.GroupEnd);
            }
            if( i != 0 && i != (tekiyouDrugs.size() - 1) ) {
                tekiyouLine.opts.add(TekiyouLineOpt.GroupExtend);
            }
            addTekiyou(new TekiyouNop());
        }
        drugBegin = null;
        tekiyouDrugs = new ArrayList<>();
    }

    public void setShouki(String shouki){
        TekiyouLine line = new TekiyouLine("", shouki, "");
        //shoujoushoukiList.add(line);
        shoujoushoukiList.add(new TekiyouNop());
    }

}

