package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backend.Backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tester {

    public void test(Backend backend){
        new PatientTester(backend).test();
    }

}
