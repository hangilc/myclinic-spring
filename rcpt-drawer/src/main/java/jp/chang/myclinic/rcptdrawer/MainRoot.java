package jp.chang.myclinic.rcptdrawer;

import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.rcptdrawer.drawerpreview.DrawerCanvas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class MainRoot extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private DrawerCanvas drawerCanvas = new DrawerCanvas(PaperSize.A4, 0.6);

    MainRoot() {
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        getChildren().add(drawerCanvas);
        RcptDrawer rcptDrawer = new RcptDrawer();
        List<List<Op>> pages = rcptDrawer.getPages();
        drawerCanvas.setOps(pages.get(0));
    }



}
