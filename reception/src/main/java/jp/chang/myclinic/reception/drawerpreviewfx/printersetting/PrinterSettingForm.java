package jp.chang.myclinic.reception.drawerpreviewfx.printersetting;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.reception.javafx.Form;
import jp.chang.myclinic.reception.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrinterSettingForm extends Form {

    public static class FormResult {
        public String name;
        public byte[] devnames;
        public byte[] devmode;
        public AuxSetting auxSetting;
    }

    private static Logger logger = LoggerFactory.getLogger(PrinterSettingForm.class);
    private TextField nameInput;
    private PrinterInputPane printerInputPane;
    private TextField dxInput;
    private TextField dyInput;
    private TextField scaleInput;

    public PrinterSettingForm(){
        Form form = this;
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
    }

    public void setData(String name, byte[] devnames, byte[] devmode, AuxSetting auxSetting){
        nameInput.setText(name);
        printerInputPane.setData(devmode, devnames);
        dxInput.setText("" + auxSetting.getDx());
        dyInput.setText("" + auxSetting.getDy());
        scaleInput.setText("" + auxSetting.getScale());
    }

    public FormResult getConfirmedResult(){
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
