package jp.chang.myclinic.reception.javafx.edit_kouhi;

import jp.chang.myclinic.util.kanjidate.Gengou;
import jp.chang.myclinic.utilfx.dateinput.DateFormInputs;

class KouhiFormInputs {

    String futanshaBangou;
    String jukyuushaBangou;
    DateFormInputs validFromInputs;
    DateFormInputs validUptoInputs;

    static KouhiFormInputs createForEnter(){
        KouhiFormInputs inputs = new KouhiFormInputs();
        inputs.futanshaBangou = "";
        inputs.jukyuushaBangou = "";
        inputs.validFromInputs = new DateFormInputs(Gengou.Current, "", "", "");
        inputs.validUptoInputs = new DateFormInputs(Gengou.Current, "", "", "");
        return inputs;
    }

}
