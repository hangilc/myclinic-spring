package jp.chang.myclinic.backend.test;

import jp.chang.myclinic.backend.Backend;

public class Tester {

    public void test(Backend backend){
        new PatientTester(backend).test();
    }

}
