package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseDTO;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;

class DiseaseTester extends TesterBase {

    DiseaseTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    @DbTest
    public void testGetDiseaseFull(Backend backend){
        PatientDTO patient = mock.pickPatient();
        backend.enterPatient(patient);
        confirm(patient.patientId > 0);
        LocalDate now = LocalDate.now();
        ByoumeiMasterDTO byoumeiMaster = backend.getByoumeiMasterByName("糖尿病", now);
        confirm(byoumeiMaster != null);
        DiseaseDTO disease = new DiseaseDTO();
        disease.patientId = patient.patientId;
        disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
        disease.startDate = now.toString();
        disease.endDate = "0000-00-00";
        disease.endReason = DiseaseEndReason.NotEnded.getCode();
        backend.enterDisease(disease);
        confirm(disease.diseaseId > 0);
        DiseaseFullDTO result = backend.getDiseaseFull(disease.diseaseId);
        confirm(result.disease.diseaseId == disease.diseaseId);
        confirm(result.adjList == null || result.adjList.size() == 0);
    }

    @DbTest
    public void testListCurrentDiseasse(Backend backend){
    }

    @DbTest
    public void testListAllDiseasse(Backend backend){
    }
}
