package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;

public class TextTester extends TesterBase {

    TextTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testEnter(Backend backend){

    }
}
