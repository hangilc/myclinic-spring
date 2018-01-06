package jp.chang.myclinic.reception.javafx;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptPreviewStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(ReceiptPreviewStage.class);

    public ReceiptPreviewStage(){
        VBox root = new VBox(4);
        {
            HBox row = new HBox(4);
            Button printButton = new Button("印刷");
            row.getChildren().addAll(printButton);
            root.getChildren().add(row);
        }
        {
            Canvas canvas = new Canvas(464, 318);
            root.getChildren().add(canvas);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_LEFT);
            Label printSettingLabel = new Label("（未選択）");
            Hyperlink chooseSettingLink = new Hyperlink("選択");
            Hyperlink saveSettingLink = new Hyperlink("選択");
            Hyperlink manageSettingLink = new Hyperlink("管理");
            row.getChildren().addAll(new Label("印刷設定："), printSettingLabel,
                    chooseSettingLink, saveSettingLink, manageSettingLink);
            root.getChildren().add(row);
        }
        root.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }
}
