package jp.chang.myclinic.scanner;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import jp.chang.myclinic.utilfx.GuiUtil;

class SetDpiDialog extends SetSettingDialogBase {

    //private static Logger logger = LoggerFactory.getLogger(SetDpiDialog.class);
    private IntegerProperty dpi = new SimpleIntegerProperty(Globals.dpi);

    SetDpiDialog() {
        super("ＤＰＩの設定");
        setupUi();
    }

    int getDpi(){
        return dpi.getValue();
    }

    @Override
    protected String getDialogStyleClass() {
        return "set-dpi-dialog";
    }

    @Override
    ObservableValue<String> getCurrentValue() {
        return Bindings.convert(dpi);
    }

    @Override
    void doChange(){
        GuiUtil.askForString("DPIの値の入力", dpi.getValue().toString()).ifPresent(input -> {
            try {
                int intValue = Integer.parseInt(input);
                dpi.setValue(intValue);
            } catch(NumberFormatException ex){
                GuiUtil.alertError("dpiの入力が不適切です。");
            }
        });
    }

}
