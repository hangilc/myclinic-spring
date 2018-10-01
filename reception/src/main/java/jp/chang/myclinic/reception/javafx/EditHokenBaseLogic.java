package jp.chang.myclinic.reception.javafx;

import jp.chang.myclinic.utilfx.dateinput.DateInputLogic;

public class EditHokenBaseLogic {

    //private static Logger logger = LoggerFactory.getLogger(EditHokenBaseLogic.class);

    DateInputLogic validFrom = new DateInputLogic();
    DateInputLogic validUpto = new DateInputLogic();

    public EditHokenBaseLogic(){
        validUpto.setNullAllowed(true);
    }

    public DateInputLogic getValidFrom() {
        return validFrom;
    }

    public DateInputLogic getValidUpto() {
        return validUpto;
    }

}
