package jp.chang.myclinic.rcptdrawer;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.rcptdrawer.drawerpreview.DrawerCanvas;

import java.util.Collections;
import java.util.List;

class MainRoot extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private DrawerCanvas drawerCanvas = new DrawerCanvas(PaperSize.A4, 1.3);
    private List<List<Op>> rcptPages = Collections.emptyList();
    private int currentPageIndex;

    MainRoot() {
        super(4);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        Node previewNode = createPreview();
        getChildren().addAll(
                previewNode,
                createRightColumn()
        );
        HBox.setHgrow(previewNode, Priority.ALWAYS);
    }

    public void setRcptPages(List<List<Op>> rcptPages){
        this.rcptPages = rcptPages;
        this.currentPageIndex = 0;
        updatePreview();
    }

    private void updatePreview(){
        List<Op> current = rcptPages.get(currentPageIndex);
        if( current != null ){
            drawerCanvas.setOps(current);
        } else {
            drawerCanvas.setOps(Collections.emptyList());
        }
    }

    private Node createPreview(){
        ScrollPane scrollPane = new ScrollPane(drawerCanvas);
        scrollPane.setPrefWidth(DrawerCanvas.mmToPixel(PaperSize.A4.getWidth() * 0.7));
        scrollPane.setPrefHeight(DrawerCanvas.mmToPixel(PaperSize.A4.getHeight() * 0.7));
        return scrollPane;
    }

    private Node createRightColumn(){
        VBox vbox = new VBox(4);
        Button nextButton = new Button("次へ");
        Button prevButton = new Button("前へ");
        nextButton.setOnAction(evt -> gotoNextPage());
        prevButton.setOnAction(evt -> gotoPrevPage());
        vbox.getChildren().addAll(nextButton, prevButton);
        return vbox;
    }

    private void gotoNextPage(){
        gotoPage(currentPageIndex + 1);
    }

    private void gotoPrevPage(){
        gotoPage(currentPageIndex - 1);
    }

    private void gotoPage(int rcptPage){
        if( rcptPage >= 0 && rcptPage < rcptPages.size() ){
            currentPageIndex = rcptPage;
            updatePreview();
        }
    }
}
