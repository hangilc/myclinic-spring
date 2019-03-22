package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    @DbTest
    public void testUpdate(){
        System.out.println("patient:testUpdate");
        PatientDTO p = mock.pickPatient();
        p.sex = "M";
        backend.enterPatient(p);
        p.lastName += "変更";
        p.firstName += "変更";
        p.lastNameYomi += "変更";
        p.firstNameYomi += "変更";
        p.birthday = LocalDate.parse(p.birthday).minus(1, ChronoUnit.YEARS).toString();
        p.sex = "F";
        p.address += "変更";
        p.phone += "変更";
        backend.updatePatient(p);
        PatientDTO pp = backend.getPatient(p.patientId);
        confirm(p.equals(pp));
    }

}
