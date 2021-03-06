package jp.chang.myclinic.pharma.javafx.drawerpreview;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ListSettingDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ListSettingDialog.class);
    private List<String> names;
    private PrinterEnv printerEnv;
    private DispGrid dispGrid;
    private List<Op> testPrintOps = Collections.emptyList();

    public ListSettingDialog(PrinterEnv printerEnv) {
        this.names = Collections.emptyList();
        this.printerEnv = printerEnv;
        try {
            this.names = printerEnv.listNames();
        } catch (Exception e) {
            logger.error("Failed to list printer settings.", e);
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
        }
        setTitle("印刷設定名の一覧");
        DispGrid root = new DispGrid();
        root.setStyle("-fx-padding:10");
        names.forEach(name -> root.addRow(name + ":", createCommands(name)));
        this.dispGrid = root;
        setScene(new Scene(root));
    }

    public void setTestPrintOps(List<Op> testPrintOps) {
        this.testPrintOps = testPrintOps;
    }

    private Node createCommands(String name){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Button editButton = new Button("編集");
        Button deleteButton = new Button("削除");
        editButton.setOnAction(evt -> doEdit(name));
        deleteButton.setOnAction(evt -> doDelete(name));
        hbox.getChildren().addAll(editButton, deleteButton);
        return hbox;
    }

    private void doEdit(String name){
        try {
            byte[] devmode = printerEnv.readDevmode(name);
            byte[] devnames = printerEnv.readDevnames(name);
            AuxSetting auxSetting = printerEnv.readAuxSetting(name);
            EditSettingDialog editSettingDialog = new EditSettingDialog(printerEnv, name, devmode, devnames, auxSetting);
            editSettingDialog.setTestPrintOps(testPrintOps);
            editSettingDialog.showAndWait();
            sizeToScene();
        } catch (Exception e) {
            logger.error("Failed to get printer setting info.", e);
            GuiUtil.alertException("印刷設定情報の取得に失敗しました。", e);
        }
    }

    private void doDelete(String name){
        if( !GuiUtil.confirm(name + ": この印刷設定を削除していいですか？") ){
            return;
        }
        try {
            printerEnv.deleteSetting(name);
            int index = names.indexOf(name);
            assert index >= 0;
            names.remove(index);
            dispGrid.removeRow(index);
            sizeToScene();
            onDelete(name);
        } catch (Exception e) {
            logger.error("Failed to delete printer setting.", e);
            GuiUtil.alertException("印刷設定の削除に失敗しました。", e);
        }
    }

    protected void onDelete(String name){

    }

}
