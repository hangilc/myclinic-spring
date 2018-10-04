package jp.chang.myclinic.util.logic;

import org.junit.Before;

class LogicBase {

    ErrorMessages em;

    @Before
    public void setupErrorMessages(){
        this.em = new ErrorMessages();
    }

}
