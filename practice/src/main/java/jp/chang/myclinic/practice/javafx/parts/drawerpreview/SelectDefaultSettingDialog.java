package jp.chang.myclinic.practice.javafx.parts.drawerpreview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.practice.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class SelectDefaultSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SelectDefaultSettingDialog.class);
    private static final String NO_SELECTION_TOKEN = "";

    public SelectDefaultSettingDialog(String current, List<String> names,  PrinterEnv printerEnv) {
        Parent root = createRoot(current, names, printerEnv);
        root.setStyle("-fx-padding:10px");
        setScene(new Scene(root));
    }

    protected abstract void onChange(String newDefaultSetting);

    private Parent createRoot(String current, List<String> names,  PrinterEnv printerEnv) {
        VBox root = new VBox(4);
        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        root.getChildren().add(createRow(group, "（手動選択）", NO_SELECTION_TOKEN));
        names.forEach(name -> {
            root.getChildren().add(createRow(group, name, name));
        });
        group.setValue(current == null ? NO_SELECTION_TOKEN : current);
        group.valueProperty().addListener((obs, oldValue, newValue) -> {
            if( NO_SELECTION_TOKEN.equals(newValue) ){
                newValue = null;
            }
            SelectDefaultSettingDialog.this.onChange(newValue);
            SelectDefaultSettingDialog.this.close();
        });
        return root;
    }

    private Node createRow(RadioButtonGroup<String> group, String label, String nameValue){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        RadioButton radio = group.createRadioButton(label, nameValue);
        Hyperlink detailLink = new Hyperlink("詳細");

        hbox.getChildren().addAll(
                radio,
                detailLink
        );
        return hbox;
    }

}
