package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

class PatientTester extends TesterBase {

    PatientTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testEnter(Backend backend){
        PatientDTO p = mock.pickPatient();
        backend.enterPatient(p);
        PatientDTO pp = backend.getPatient(p.patientId);
        confirm(p.equals(pp));
    }

    @DbTest
    public void testUpdate(Backend backend){
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

    @DbTest
    public void testSeachPatient(Backend backend){
        List<PatientDTO> patients;
        patients = backend.searchPatient("鈴木");
        patients = backend.searchPatient("鈴木 子");
    }

}
