package jp.chang.myclinic.reception.drawerpreviewfx;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;

import java.util.List;

public class DrawerPreviewStage extends Stage {

    public DrawerPreviewStage(List<Op> ops){
        VBox root = new VBox(4);
        PreviewCanvas canvas = new PreviewCanvas(ops);
        root.getChildren().addAll(canvas);
        setScene(new Scene(root));
    }
}
