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
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.reception.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class PrinterSettingFormStage extends Stage {

    public static class FormResult {
        public String name;
        public byte[] devnames;
        public byte[] devmode;
        public AuxSetting auxSetting;
    }

    private static Logger logger = LoggerFactory.getLogger(PrinterSettingFormStage.class);
    private TextField nameInput;
    private PrinterInputPane printerInputPane;
    private TextField dxInput;
    private TextField dyInput;
    private TextField scaleInput;
    private Consumer<FormResult> doEnterAction;

    public PrinterSettingFormStage(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting){
        this();
        setName(name);
        setData(devnames, devmode, auxSetting);
    }

    public PrinterSettingFormStage(){
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

    public void setDoEnterAction(Consumer<FormResult> consumer){
        this.doEnterAction = consumer;
    }

    private String getName(){
        return nameInput.getText();
    }

    void setName(String name){
        nameInput.setText(name);
    }

    void setData(byte[] devnames, byte[] devmode, AuxSetting auxSetting){
        printerInputPane.setData(devmode, devnames);
        dxInput.setText("" + auxSetting.getDx());
        dyInput.setText("" + auxSetting.getDy());
        scaleInput.setText("" + auxSetting.getScale());
    }

    private void doEnter(){
        if( doEnterAction != null ){
            FormResult result = confirmInputs();
            if( result != null ){
                doEnterAction.accept(result);
            }
        }
    }

    private FormResult confirmInputs(){
        FormResult result = new FormResult();
        result.auxSetting = new AuxSetting();
        String name = nameInput.getText().trim();
        if( name.isEmpty() ){
            GuiUtil.alertError("名前が入力されていません。");
            return null;
        } else {
            result.name = name;
        }
        byte[] devmode = printerInputPane.getDevmode();
        byte[] devnames = printerInputPane.getDevnames();
        if( devmode == null || devnames == null ){
            GuiUtil.alertError("プリンターが設定されていません。");
            return null;
        } else {
            result.devnames = devnames;
            result.devmode = devmode;
        }
        double dx;
        try {
            dx = Double.parseDouble(dxInput.getText());
            result.auxSetting.setDx(dx);
        } catch(NumberFormatException ex){
            GuiUtil.alertError("dx の設定が不適切です。。");
            return null;
        }
        double dy;
        try {
            dy = Double.parseDouble(dyInput.getText());
            result.auxSetting.setDy(dy);
        } catch(NumberFormatException ex){
            GuiUtil.alertError("dy の設定が不適切です。。");
            return null;
        }
        double scale;
        try {
            scale = Double.parseDouble(scaleInput.getText());
            result.auxSetting.setScale(scale);
        } catch(NumberFormatException ex){
            GuiUtil.alertError("scale の設定が不適切です。。");
            return null;
        }
        return result;
    }
}
