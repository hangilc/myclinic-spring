package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import jp.chang.myclinic.util.logic.ErrorMessages;
import org.junit.Before;

class LogicTestBase {

    ErrorMessages em;

    @Before
    public void resetErrorMessages(){
        this.em = new ErrorMessages();
    }
}
