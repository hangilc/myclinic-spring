package jp.chang.myclinic.hotline;

import jp.chang.myclinic.util.logic.ErrorMessages;
import org.junit.Before;

class LogicTestBase {

    ErrorMessages em;

    @Before
    public void setupErrorMessages(){
        this.em = new ErrorMessages();
    }

}
