package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.logdto.practicelog.PatientCreated;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

class PatientTester extends TesterBase {

    PatientTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testEnter(){
        int logIndex = getCurrentPracticeLogIndex();
        PatientDTO p = mock.pickPatient();
        dbBackend.txProc(backend -> backend.enterPatient(p));
        confirm(p.patientId != 0);
        PatientDTO pp = dbBackend.query(backend -> backend.getPatient(p.patientId));
        confirm(p.equals(pp));
        List<PracticeLogDTO> logs = getPracticeLogList(logIndex, log -> {
            if( log.kind.equals("patient-created") ){
                PatientCreated created = log.asPatientCreated();
                return created.created.patientId == p.patientId;
            }
            return false;
        });
        confirm(logs.size() == 1 && logs.get(0).asPatientCreated().created.equals(p));
    }

    @DbTest
    public void testUpdate(){
        int logIndex = getCurrentPracticeLogIndex();
        PatientDTO p = mock.pickPatient();
        p.sex = "M";
        dbBackend.txProc(backend -> backend.enterPatient(p));
        PatientDTO pp = PatientDTO.copy(p);
        pp.lastName += "変更";
        pp.firstName += "変更";
        pp.lastNameYomi += "変更";
        pp.firstNameYomi += "変更";
        pp.birthday = LocalDate.parse(p.birthday).minus(1, ChronoUnit.YEARS).toString();
        pp.sex = "F";
        pp.address += "変更";
        pp.phone += "変更";
        dbBackend.txProc(backend -> backend.updatePatient(pp));
        PatientDTO ppp = dbBackend.query(backend -> backend.getPatient(p.patientId));
        confirm(ppp.equals(pp));
        List<PracticeLogDTO> logs = getPracticeLogList(logIndex, log -> {
            if( log.isPatientCreated() ){
                return log.asPatientCreated().created.patientId == p.patientId;
            }
            if( log.isPatientUpdated() ){
                return log.asPatientUpdated().updated.patientId == p.patientId;
            }
            return false;
        });
        confirm(logs.size() == 2);
        confirm(logs.get(0).asPatientCreated().created.equals(p));
        confirm(logs.get(1).isPatientUpdated() && logs.get(1).asPatientUpdated().prev.equals(p) &&
                logs.get(1).asPatientUpdated().updated.equals(pp));
    }

//    @DbTest
//    public void testSeachPatient(Backend backend){
//        List<PatientDTO> patients;
//        patients = backend.searchPatient("鈴木");
//        patients = backend.searchPatient("鈴木 子");
//    }

}
