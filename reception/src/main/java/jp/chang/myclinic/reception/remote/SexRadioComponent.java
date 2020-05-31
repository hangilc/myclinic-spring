package jp.chang.myclinic.reception.remote;

import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.utilfx.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SexRadioComponent implements TextComponent {

    private RadioButtonGroup<Sex> radioGroup;

    public SexRadioComponent(RadioButtonGroup<Sex> radioGroup) {
        this.radioGroup = radioGroup;
    }

    @Override
    public void setComponentText(String text) {
        Sex sex = Sex.fromKanji(text);
        if( sex != null ){
            this.radioGroup.setValue(sex);
        }
    }

    @Override
    public String getComponentText() {
        Sex sex = radioGroup.getValue();
        if( sex != null ){
            return sex.getKanji();
        } else {
            return null;
        }
    }
}
