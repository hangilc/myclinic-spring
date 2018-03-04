package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(CreateSettingDialog.class);

    public CreateSettingDialog() {
        setTitle("新規印刷設定");
        BorderPane root = new BorderPane();
        setScene(new Scene(root));
    }

}
