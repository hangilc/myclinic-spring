package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DrawerPreviewDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(DrawerPreviewDialog.class);

    private ScrollPane scrollPane;
    private DrawerCanvas drawerCanvas;

    public DrawerPreviewDialog() {
        VBox root = new VBox(4);
        root.getStyleClass().add("drawer-preview-dialog");
        root.getChildren().addAll(
            createCanvas()
        );
        setScene(new Scene(root));
    }

    public void setContentSize(double mmWidth, double mmHeight){
        drawerCanvas.setContentSize(mmWidth, mmHeight);
    }

    public void setScaleFactor(double scaleFactor){
        drawerCanvas.setScaleFactor(scaleFactor);
    }

    public void setOps(List<Op> ops){
        drawerCanvas.setOps(ops);
    }

    private Node createCanvas(){
        drawerCanvas = new DrawerCanvas();
        scrollPane = new ScrollPane(drawerCanvas);
        return scrollPane;
    }

}
