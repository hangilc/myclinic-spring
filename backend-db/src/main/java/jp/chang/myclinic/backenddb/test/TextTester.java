package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;

public class TextTester extends TesterBase {

    TextTester(Backend backend) {
        super(backend);
    }

    @DbTest
    public void testEnter(){

    }
}
