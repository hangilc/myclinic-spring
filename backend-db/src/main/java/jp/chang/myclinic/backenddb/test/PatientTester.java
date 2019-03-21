package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backend.Backend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class PatientTester extends TesterBase {

    public PatientTester(Backend backend) {
        super(backend);
    }

    @DbTest
    public void testEnter(){
        System.out.println("patient:testEnter");
        PatientDTO p = mock.pickPatient();
        backend.enterPatient(p);
        PatientDTO pp = backend.getPatient(p.patientId);
        confirm(p.equals(pp));
    }

}
