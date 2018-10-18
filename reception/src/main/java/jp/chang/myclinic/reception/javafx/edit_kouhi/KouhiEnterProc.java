package jp.chang.myclinic.reception.javafx.edit_kouhi;

import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

import java.util.function.Consumer;

class KouhiEnterProc {

    //private static Logger logger = LoggerFactory.getLogger(KouhiEnterProc.class);
    private int patientId;

    KouhiEnterProc(int patientId) {
        this.patientId = patientId;
    }

    KouhiFormInputs createInputs(){
        KouhiFormInputs inputs = new KouhiFormInputs();
        inputs.futanshaBangou = "";
        inputs.jukyuushaBangou = "";
        inputs.validFromInputs = new DateFormInputs(Gengou.Current, "", "", "");
        inputs.validUptoInputs = new DateFormInputs(Gengou.Current, "", "", "");
        return inputs;
    }

}
