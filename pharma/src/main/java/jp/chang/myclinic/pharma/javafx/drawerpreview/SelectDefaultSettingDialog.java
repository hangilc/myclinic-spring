package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DevmodeInfo;
import jp.chang.myclinic.drawer.printer.DevnamesInfo;
import jp.chang.myclinic.drawer.printer.manager.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SelectDefaultSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SelectDefaultSettingDialog.class);
    private static final String NO_SELECTION_TOKEN = "";
    private PrinterEnv printerEnv;
    private List<Op> testPrintOps = new ArrayList<>();

    public SelectDefaultSettingDialog(String current, PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
        List<String> names = Collections.emptyList();
        try {
            names = printerEnv.listNames();
        } catch (IOException e) {
            logger.error("Failed to list printer setting names.", e);
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
        }
        Parent root = createRoot(current, names);
        root.setStyle("-fx-padding:10px");
        setScene(new Scene(root));
    }

    void setTestPrintOps(List<Op> testPrintOps) {
        this.testPrintOps = testPrintOps;
    }

    protected abstract void onChange(String newDefaultSetting);

    private Parent createRoot(String current, List<String> names) {
        VBox root = new VBox(4);
        RadioButtonGroup<String> group = new RadioButtonGroup<>();
        root.getChildren().add(createRow(group, "（手動選択）", NO_SELECTION_TOKEN));
        names.forEach(name -> root.getChildren().add(createRow(group, name, name)));
        {
            String currentValue = current == null ? NO_SELECTION_TOKEN : current;
            group.setValue(currentValue);
        }
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
        hbox.getChildren().add(radio);
        if (!NO_SELECTION_TOKEN.equals(nameValue) && nameValue != null) {
            Hyperlink detailLink = new Hyperlink("詳細");
            Hyperlink testPrintLink = new Hyperlink("テスト印刷");
            detailLink.setOnAction(evt -> doDetail(nameValue));
            testPrintLink.setOnAction(evt -> doTestPrint(nameValue));
            hbox.getChildren().addAll(
                    detailLink,
                    testPrintLink
            );
        }
        return hbox;
    }

    private void doDetail(String nameValue){
        try {
            DevmodeInfo devmodeInfo = new DevmodeInfo(printerEnv.readDevmode(nameValue));
            DevnamesInfo devnamesInfo = new DevnamesInfo(printerEnv.readDevnames(nameValue));
            AuxSetting auxSetting = printerEnv.readAuxSetting(nameValue);
            String text = String.format("%s\n%s; %s; dx=%s; dy=%s; scale=%s",
                    devnamesInfo.getDevice(), devmodeInfo.getDefaultSourceLabel(),
                    devmodeInfo.getOrientationLabel(),
                    auxSetting.getDx(), auxSetting.getDy(), auxSetting.getScale());
            GuiUtil.alertInfo(text);
        } catch (IOException e) {
            GuiUtil.alertException("印刷設定情報の取得に失敗しました。", e);
        }
    }

    private void doTestPrint(String name){
        try {
            printerEnv.print(Collections.singletonList(testPrintOps), name);
        } catch(Exception ex){
            GuiUtil.alertException("テスト印刷に失敗しました。", ex);
        }
    }

}
