package jp.chang.myclinic.rcptdrawer;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.rcptdrawer.drawerpreview.DrawerCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class MainRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private DrawerCanvas drawerCanvas = new DrawerCanvas(PaperSize.A4, 1.1);

    MainRoot() {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        ScrollPane scrollPane = new ScrollPane(drawerCanvas);
        scrollPane.setPrefWidth(DrawerCanvas.mmToPixel(PaperSize.A4.getWidth() * 0.7));
        scrollPane.setPrefHeight(DrawerCanvas.mmToPixel(PaperSize.A4.getHeight() * 0.7));
        getChildren().add(scrollPane);
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
        List<List<Op>> pages = rcptDrawer.getPages();
        drawerCanvas.setOps(pages.get(0));
    }



}
