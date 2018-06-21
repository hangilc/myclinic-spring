package jp.chang.myclinic.reception.drawerpreviewfx.printersetting;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class EditPrinterSettingStage extends Stage {

    public interface Callback {
        void onEnter(String newName);
        void onDelete();
    }

    private static Logger logger = LoggerFactory.getLogger(EditPrinterSettingStage.class);
    private PrinterEnv printerEnv;
    private String origName;
    private PrinterSettingForm form;
    private Callback callback;

    public EditPrinterSettingStage(PrinterEnv printerEnv, String name, byte[] devnames, byte[] devmode,
                                   AuxSetting auxSetting){
        this.printerEnv = printerEnv;
        this.origName = name;
        setTitle(String.format("印刷設定の編集：%s", name));
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        {
            this.form = new PrinterSettingForm();
            form.setData(name, devnames, devmode, auxSetting);
            root.getChildren().add(form);
            VBox.setVgrow(form, Priority.ALWAYS);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button enterButton = new Button("入力");
            Button deleteButton = new Button("削除");
            Button cancelButton = new Button("キャンセル");
            enterButton.setOnAction(event -> doEnter());
            deleteButton.setOnAction(event -> doDelete());
            cancelButton.setOnAction(event -> close());
            hbox.getChildren().addAll(enterButton, deleteButton, cancelButton);
            root.getChildren().add(hbox);
        }
        setScene(new Scene(root));
    }

    public void setCallback(Callback callback){
        this.callback = callback;
    }

    private void doEnter(){
        PrinterSettingForm.FormResult result = form.getConfirmedResult();
        if( result != null ){
            try {
                printerEnv.savePrintSetting(result.name, result.devnames, result.devmode, result.auxSetting);
                if( !result.name.equals(origName) ){
                    printerEnv.deletePrintSetting(origName);
                }
                if( callback != null ){
                    callback.onEnter(result.name);
                }
            } catch (Exception ex) {
                logger.error("Failed to save printer setting.", ex);
                GuiUtil.alertException("Failed to save printer setting.", ex);
            }
        }
    }

    private void doDelete(){
        if( !GuiUtil.confirm(String.format("この印刷設定（%s）を削除していいですか？", origName)) ){
            return;
        }
        try {
            printerEnv.deletePrintSetting(origName);
            if( callback != null ){
                callback.onDelete();
            }
        } catch (IOException e) {
            logger.error("Failed to delete printer setting: {}.", origName, e);
            GuiUtil.alertException("Failed to delete printer setting.", e);
        }
    }

}
