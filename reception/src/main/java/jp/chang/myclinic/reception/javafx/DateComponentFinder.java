package jp.chang.myclinic.reception.javafx;

import jp.chang.myclinic.reception.remote.ComponentFinder;
import jp.chang.myclinic.utilfx.dateinput.DateForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateComponentFinder implements ComponentFinder {

    private DateForm form;

    public DateComponentFinder(DateForm form) {
        this.form = form;
    }

    @Override
    public Object findComponent(String selector) {
        if( selector == null ){
            return null;
        }
        switch(selector){
            case "Gengou": return form.getGengouInput();
            case "Nen": return form.getNenInput();
            case "Month": return form.getMonthInput();
            case "Day": return form.getDayInput();
            default: return null;
        }
    }
}
