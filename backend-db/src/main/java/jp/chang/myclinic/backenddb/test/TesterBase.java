package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.mockdata.MockData;

import java.lang.reflect.Method;

class TesterBase {

    private DbBackend dbBackend;
    static MockData mock = new MockData();
    PatientDTO patient1;

    TesterBase(DbBackend dbBackend) {
        this.dbBackend = dbBackend;
        patient1 = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(patient1));
    }

    public void test(){
        String clsName = getClass().getSimpleName();
        try {
            for (Method method : this.getClass().getMethods()) {
                if (method.isAnnotationPresent(DbTest.class)) {
                    System.out.printf("%s:%s\n", clsName, method.getName());
                    dbBackend.txProc(backend -> {
                        try {
                            method.invoke(this, backend);
                        } catch(Exception e){
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    void confirm(boolean ok){
        if( !ok ){
            throw new RuntimeException("Confirmation failed.");
        }
    }

}
