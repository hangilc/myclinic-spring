package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;

public class Tester {

    public void test(Backend backend){
        new PatientTester(backend).test();
    }

}
