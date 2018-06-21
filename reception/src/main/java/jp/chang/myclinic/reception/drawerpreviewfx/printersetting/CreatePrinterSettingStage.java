package jp.chang.myclinic.reception.drawerpreviewfx.printersetting;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.myclinicenv.printer.PrinterEnv;
import jp.chang.myclinic.reception.javafx.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CreatePrinterSettingStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(CreatePrinterSettingStage.class);
    private PrinterEnv printerEnv;
    private TextField nameInput;
    private PrinterInputPane printerInputPane;
    private TextField dxInput;
    private TextField dyInput;
    private TextField scaleInput;
    private boolean created;

    public CreatePrinterSettingStage(PrinterEnv printerEnv) {
        this.printerEnv = printerEnv;
        setTitle("新規印刷設定の入力");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        {
            Form form = new Form();
            nameInput = new TextField();
            nameInput.setMaxWidth(160);
            form.add("名前", nameInput);
            printerInputPane = new PrinterInputPane();
            {
                RowConstraints rc = new RowConstraints();
                rc.setVgrow(Priority.ALWAYS);
                rc.setFillHeight(true);
                form.add("プリンター", printerInputPane, rc);
            }
            dxInput = new TextField("0");
            dxInput.setMaxWidth(40);
            dxInput.setTooltip(new Tooltip("右方向の変位"));
            form.add("dx", dxInput);
            dyInput = new TextField("0");
            dyInput.setMaxWidth(40);
            dyInput.setTooltip(new Tooltip("下方向への変位"));
            form.add("dy", dyInput);
            scaleInput = new TextField("1.0");
            scaleInput.setMaxWidth(40);
            scaleInput.setTooltip(new Tooltip("拡大"));
            form.add("scale", scaleInput);
            root.getChildren().add(form);
            VBox.setVgrow(form, Priority.ALWAYS);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button enterButton = new Button("入力");
            Button cancelButton = new Button("キャンセル");
            enterButton.setOnAction(event -> doEnter());
            cancelButton.setOnAction(event -> close());
            hbox.getChildren().addAll(enterButton, cancelButton);
            root.getChildren().add(hbox);
        }
        setScene(new Scene(root));
    }

    public boolean isCreated() {
        return created;
    }

    private void doEnter(){
        String name = nameInput.getText().trim();
        if( name.isEmpty() ){
            GuiUtil.alertError("名前が入力されていません。");
            return;
        }
        byte[] devmode = printerInputPane.getDevmode();
        byte[] devnames = printerInputPane.getDevnames();
        if( devmode == null || devnames == null ){
            GuiUtil.alertError("プリンターが設定されていません。");
            return;
        }
        double dx = 0;
        try {
            dx = Double.parseDouble(dxInput.getText());
        } catch(NumberFormatException ex){
            GuiUtil.alertError("dx の設定が不適切です。。");
            return;
        }
        double dy = 0;
        try {
            dy = Double.parseDouble(dyInput.getText());
        } catch(NumberFormatException ex){
            GuiUtil.alertError("dy の設定が不適切です。。");
            return;
        }
        double scale = 0;
        try {
            scale = Double.parseDouble(scaleInput.getText());
        } catch(NumberFormatException ex){
            GuiUtil.alertError("scale の設定が不適切です。。");
            return;
        }
        AuxSetting auxSetting = new AuxSetting();
        auxSetting.setDx(dx);
        auxSetting.setDy(dy);
        auxSetting.setScale(scale);
        try {
            List<String> existingNames = printerEnv.listSettingNames();
            if( existingNames.contains(name) ){
                GuiUtil.alertError(name + " という設定はすでに存在します。");
                return;
            }
            printerEnv.savePrintSetting(name, devnames, devmode, auxSetting);
            created = true;
            close();
        } catch(Exception ex){
            logger.error("Failed to printersetting printer setting.", ex);
            GuiUtil.alertException("Failed to printersetting printer setting.", ex);
        }
    }

}
