package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditSettingDialog.class);
    private String name;

    public EditSettingDialog(String name, byte[] devmode, byte[] devnames, AuxSetting auxSetting) {
        this.name = name;
        setTitle("印刷設定の編集");
        BorderPane root = new BorderPane();
        root.setCenter(createCenter());
        setScene(new Scene(root));
    }

    private Node createCenter(){
        DispGrid root = new DispGrid();
        root.addRow("名前：", new Label(name));
        return root;
    }

}
