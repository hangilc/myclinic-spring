package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;

public class Tester {

    public void test(DbBackend dbBackend){
        new PatientTester(dbBackend).test();
        new VisitTester(dbBackend).test();
        new DrugTester(dbBackend).test();
        new ShinryouTester(dbBackend).test();
    }

}
