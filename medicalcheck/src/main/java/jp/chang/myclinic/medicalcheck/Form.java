package jp.chang.myclinic.medicalcheck;

import javafx.scene.control.TextField;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.medicalcheck.dateinput.DateInput;
import jp.chang.myclinic.medicalcheck.lib.SexRadioInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Form extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(Form.class);
    private TextField nameField = new TextField();
    private DateInput birthdayInput = new DateInput();
    private SexRadioInput sexInput = new SexRadioInput(Sex.Female);

    Form() {
        birthdayInput.setGengou(Gengou.Heisei);
        addRow("氏名", nameField);
        addRow("生年月日", birthdayInput);
        addRow("性別", sexInput);
    }

}
