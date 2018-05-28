package jp.chang.myclinic.rcptdrawer;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.rcptdrawer.drawerpreview.DrawerCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

class MainRoot extends HBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private DrawerCanvas drawerCanvas = new DrawerCanvas(PaperSize.A4, 1.3);
    private List<List<List<Op>>> rcptPages = Collections.emptyList();
    private int currentRcptIndex;
    private int currentPageIndex;

    MainRoot() {
        super(4);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        getChildren().add(createPreview());
    }

    public void setRcptPages(List<List<List<Op>>> rcptPages){
        this.rcptPages = rcptPages;
        this.currentRcptIndex = 0;
        this.currentPageIndex = 0;
        updatePreview();
    }

    private void updatePreview(){
        List<List<Op>> current = getCurrentRcpt();
        if( current.isEmpty() ){
            this.currentPageIndex = 0;
            drawerCanvas.setOps(Collections.emptyList());
        } else {
            if( currentPageIndex >= 0 && currentPageIndex < current.size() ){
                drawerCanvas.setOps(current.get(currentPageIndex));
            }
        }
    }

    private List<List<Op>> getCurrentRcpt(){
        if( currentRcptIndex >= 0 && currentRcptIndex < rcptPages.size() ){
            return rcptPages.get(currentRcptIndex);
        } else {
            return Collections.emptyList();
        }
    }

    private Node createPreview(){
        ScrollPane scrollPane = new ScrollPane(drawerCanvas);
        scrollPane.setPrefWidth(DrawerCanvas.mmToPixel(PaperSize.A4.getWidth() * 0.7));
        scrollPane.setPrefHeight(DrawerCanvas.mmToPixel(PaperSize.A4.getHeight() * 0.7));
        return scrollPane;
    }

    private void testDrawer(){
        RcptDrawer rcptDrawer = new RcptDrawer();
        rcptDrawer.putPatientId(2360);
        rcptDrawer.putShinryouMonth(30, 5);
        rcptDrawer.putFukenBangou(13);
        rcptDrawer.putKikanCode("12.3456.7");
        //rcptDrawer.markHokenshubetsuShakoku();
        //rcptDrawer.markHokenshubetsuKouhi1();
        //rcptDrawer.markHokenshubetsuKoukikourei();
        rcptDrawer.markHokenshubetsuTaishoku();
        //rcptDrawer.markHokentandokuTandoku();
        //rcptDrawer.markHokentandokuHei2();
        rcptDrawer.markHokentandokuHei3();
        //rcptDrawer.markHokenfutanHonnin();
        //rcptDrawer.markHokenfutanSansai();
        //rcptDrawer.markHokenfutanKazoku();
        //rcptDrawer.markHokenfutanKourei9();
        rcptDrawer.markHokenfutanKourei7();
        rcptDrawer.setHokenshaBangou((123456));
        //rcptDrawer.markKyuufuwari10();
        //rcptDrawer.markKyuufuwari9();
        //rcptDrawer.markKyuufuwari8();
        //rcptDrawer.markKyuufuwari7();
        rcptDrawer.putKyuufuwariOther("5");
        rcptDrawer.putHihokenshashou("A123 4567");
        rcptDrawer.putKouhiFutanshaBangou1(12345678);
        rcptDrawer.putKouhiJukyuushaBangou1(6543210);
        rcptDrawer.putKouhiFutanshaBangou2(40323431);
        rcptDrawer.putKouhiJukyuushaBangou2(5472342);
        rcptDrawer.putShimei("苗字名前");
        //rcptDrawer.markSeibetsuOtoko();
        rcptDrawer.markSeibetsuOnna();
        //rcptDrawer.markSeinengappiMeiji();
        //rcptDrawer.markSeinengappiTaishou();
        //rcptDrawer.markSeinengappiShouwa();
        rcptDrawer.markSeinengappiHeisei();
        rcptDrawer.putSeinengappiNen(12);
        rcptDrawer.putSeinengappiMonth(10);
        rcptDrawer.putSeinengappiDay(31);
        //rcptDrawer.markShokumujouShokumujou();
        //rcptDrawer.markShokumujouGesen();
        rcptDrawer.markShokumujouTsuukin();
        rcptDrawer.putTokkijikou("特記事項の内容。\n２行目以降の内容。つづきの文章。");
        rcptDrawer.putShozaichiMeishouLine1("医療機関住所");
        rcptDrawer.putShozaichiMeishouLine2("03 (1234) 5678");
        rcptDrawer.putShozaichiMeishouLine3("医療機関名称");
        rcptDrawer.putShinryouNissuuHoken(3);
        rcptDrawer.putShinryouNissuuKouhi1(1);
        rcptDrawer.putShinryouNissuuKouhi2(2);
        //rcptDrawer.markShoshinJikangai();
        //rcptDrawer.markShoshinKyuujitsu();
        rcptDrawer.markShoshinShinya();
        rcptDrawer.putShoshinKai(1);
        rcptDrawer.putShoshinTen(270);
        rcptDrawer.putSaishinSaishinTanka(67);
        rcptDrawer.putSaishinSaishinTimes(2);
        rcptDrawer.putSaishinSaishinTen(134);
        rcptDrawer.putSaishinGairaiKanriTanka(10);
        rcptDrawer.putSaishinGairaiKanriTimes(2);
        rcptDrawer.putSaishinGairaiKanriTen(20);
        rcptDrawer.putSaishinJikangaiTanka(26);
        rcptDrawer.putSaishinJikangaiTimes(1);
        rcptDrawer.putSaishinJikangaiTen(26);
        rcptDrawer.putSaishinKyuujitsuTanka(22);
        rcptDrawer.putSaishinKyuujitsuTimes(3);
        rcptDrawer.putSaishinKyuujitsuTen(66);
        rcptDrawer.putSaishinShinyaTanka(12);
        rcptDrawer.putSaishinShinyaTimes(3);
        rcptDrawer.putSaishinShinyaTen(36);
        rcptDrawer.putShidouTen(123);
        rcptDrawer.putZaitakuOushinTanka(2);
        rcptDrawer.putZaitakuOushinTimes(3);
        rcptDrawer.putZaitakuOushinTen(6);
        rcptDrawer.putZaitakuYakanTanka(3);
        rcptDrawer.putZaitakuYakanTimes(4);
        rcptDrawer.putZaitakuYakanTen(12);
        rcptDrawer.putZaitakuShinyaTanka(4);
        rcptDrawer.putZaitakuShinyaTimes(5);
        rcptDrawer.putZaitakuShinyaTen(20);
        rcptDrawer.putZaitakuZaitakuTanka(5);
        rcptDrawer.putZaitakuZaitakuTimes(6);
        rcptDrawer.putZaitakuZaitakuTen(30);
        rcptDrawer.putZaitakuSonotaTen(12);
        rcptDrawer.putZaitakuYakuzaiTen(23);
        rcptDrawer.putTouyakuNaifukuYakuzaiTimes(70);
        rcptDrawer.putTouyakuNaifukuYakuzaiTen(170);
        rcptDrawer.putTouyakuNaifukuChouzaiTanka(9);
        rcptDrawer.putTouyakuNaifukuChouzaiTimes(2);
        rcptDrawer.putTouyakuNaifukuChouzaiTen(18);
        rcptDrawer.putTouyakuTonpukuYakuzaiTimes(6);
        rcptDrawer.putTouyakuTonpukuYakuzaiTen(12);
        rcptDrawer.putTouyakuGaiyouYakuzaiTimes(14);
        rcptDrawer.putTouyakuGaiyouYakuzaiTen(140);
        rcptDrawer.putTouyakuGaiyouChouzaiTanka(8);
        rcptDrawer.putTouyakuGaiyouChouzaiTimes(3);
        rcptDrawer.putTouyakuGaiyouChouzaiTen(24);
        rcptDrawer.putTouyakuShohouTanka(9);
        rcptDrawer.putTouyakuShohouTimes(4);
        rcptDrawer.putTouyakuShohouTen(36);
        rcptDrawer.putTouyakuMadokuTimes(5);
        rcptDrawer.putTouyakuMadokuTen(40);
        rcptDrawer.putTouyakuChoukiTen(8);
        rcptDrawer.putChuushaHikaTimes(1);
        rcptDrawer.putChuushaHikaTen(2);
        rcptDrawer.putChuushaJoumyakuTimes(3);
        rcptDrawer.putChuushaJoumyakuTen(4);
        rcptDrawer.putChuushaSonotaTimes(5);
        rcptDrawer.putChuushaSonotaTen(6);
        rcptDrawer.putShochiTimes(2);
        rcptDrawer.putShochiTen(24);
        rcptDrawer.putShochiYakuzaiTen(120);
        rcptDrawer.putShujutsuTimes(3);
        rcptDrawer.putShujutsuTen(34);
        rcptDrawer.putShujutsuYakuzaiTen(130);
        rcptDrawer.putKensaTimes(4);
        rcptDrawer.putKensaTen(44);
        rcptDrawer.putKensaYakuzaiTen(140);
        rcptDrawer.putGazouTimes(5);
        rcptDrawer.putGazouTen(55);
        rcptDrawer.putGazouYakuzaiTen(150);
        rcptDrawer.putSonotaShohousenTimes(6);
        rcptDrawer.putSonotaShohousenTen(65);
        rcptDrawer.putSonotaSonotaTen(160);
        rcptDrawer.putSonotaYakuzaiTen(170);
        rcptDrawer.putKyuufuHokenSeikyuuten(661);
        rcptDrawer.putKyuufuKouhi1Seikyuuten(541);
        rcptDrawer.putKyuufuKouhi2Seikyuuten(432);
        rcptDrawer.putIchibufutankinHoken(380);
        rcptDrawer.putIchibufutankinKouhi1(270);
        rcptDrawer.putIchibufutankinKouhi2(160);
        List<List<Op>> pages = rcptDrawer.getPages();
        drawerCanvas.setOps(pages.get(0));
    }

}
