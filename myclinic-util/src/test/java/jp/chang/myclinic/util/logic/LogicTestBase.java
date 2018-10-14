package jp.chang.myclinic.util.logic;

import org.junit.Before;

class LogicTestBase {

    ErrorMessages em;

    @Before
    public void setupErrorMessages(){
        this.em = new ErrorMessages();
    }

}
