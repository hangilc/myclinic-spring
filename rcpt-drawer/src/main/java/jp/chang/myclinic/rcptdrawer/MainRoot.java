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
        List<List<Op>> pages = rcptDrawer.getPages();
        drawerCanvas.setOps(pages.get(0));
    }



}
