package jp.chang.myclinic.backenddb.test;

import jp.chang.myclinic.backenddb.Backend;
import jp.chang.myclinic.backenddb.DbBackend;
import jp.chang.myclinic.backenddb.test.annotation.DbTest;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

class DiseaseTester extends TesterBase {

    DiseaseTester(DbBackend dbBackend) {
        super(dbBackend);
    }

    private DiseaseDTO createDisease(int patientId, int shoubyoumeicode){
        DiseaseDTO disease = new DiseaseDTO();
        disease.patientId = patientId;
        disease.shoubyoumeicode = shoubyoumeicode;
        disease.endReason = DiseaseEndReason.NotEnded.getCode();
        disease.startDate = LocalDate.now().toString();
        disease.endDate = "0000-00-00";
        return disease;
    }

    private DiseaseAdjDTO createAdj(int shuushokugocode){
        DiseaseAdjDTO adj = new DiseaseAdjDTO();
        adj.shuushokugocode = shuushokugocode;
        return adj;
    }

    private DiseaseNewDTO createNewDisease(int patientId, int shoubyoumeicode, Integer... shuushokugocodes){
        DiseaseNewDTO result = new DiseaseNewDTO();
        result.disease = createDisease(patientId, shoubyoumeicode);
        result.adjList = Arrays.stream(shuushokugocodes).map(this::createAdj).collect(toList());
        return result;
    }

    private DiseaseModifyDTO createModifyDisease(DiseaseDTO origDisease, int shoubyoumeicode,
                                                 Integer... shuushokugocodes){
        DiseaseModifyDTO result = new DiseaseModifyDTO();
        result.disease = DiseaseDTO.copy(origDisease);
        result.disease.shoubyoumeicode = shoubyoumeicode;
        result.shuushokugocodes = Arrays.asList(shuushokugocodes);
        return result;
    }

    @DbTest
    public void testEnterDisease(){
        PatientDTO patient = newPatient();
        DiseaseDTO disease = createDisease(patient.patientId, dis_急性咽頭炎);
        dbBackend.txProc(backend -> backend.enterDisease(disease));
        confirm(disease.diseaseId > 0);
        DiseaseDTO saved = dbBackend.query(backend -> backend.getDisease(disease.diseaseId));
        confirm(saved.equals(disease));
        DiseaseFullDTO savedFull = dbBackend.query(backend -> backend.getDiseaseFull(disease.diseaseId));
        confirm(savedFull.disease.equals(disease));
        confirm(savedFull.master.shoubyoumeicode == disease.shoubyoumeicode);
    }

    @DbTest
    public void testEnterDiseaseWithAdj(){
        PatientDTO patient = newPatient();
        DiseaseNewDTO newDisease = createNewDisease(patient.patientId, dis_糖尿病, adj_の疑い, adj_小児);
        dbBackend.txProc(backend -> backend.enterNewDisease(newDisease));
        DiseaseFullDTO saved = dbBackend.query(backend -> backend.getDiseaseFull(newDisease.disease.diseaseId));
        confirm(saved.disease.equals(newDisease.disease));
        confirm(saved.adjList.stream().map(full -> full.diseaseAdj).collect(toList()).equals(newDisease.adjList));
    }

    @DbTest
    public void testDeleteDiseaseAdj(){
        PatientDTO patient = newPatient();
        DiseaseNewDTO newDisease = createNewDisease(patient.patientId, dis_糖尿病, adj_の疑い, adj_小児);
        dbBackend.txProc(backend -> backend.enterNewDisease(newDisease));
        int ndel = dbBackend.tx(backend -> backend.deleteDiseaseAdjForDisease(newDisease.disease));
        confirm(ndel == 2);
        DiseaseFullDTO saved = dbBackend.query(backend -> backend.getDiseaseFull(newDisease.disease.diseaseId));
        confirmNotNull(saved);
        confirm(saved.disease.equals(newDisease.disease));
        confirm(saved.adjList.size() == 0);
    }

    @DbTest
    public void testModifyDisease(){
        PatientDTO patient = newPatient();
        DiseaseNewDTO newDisease = createNewDisease(patient.patientId, dis_糖尿病, adj_の疑い, adj_小児);
        dbBackend.txProc(backend -> backend.enterNewDisease(newDisease));
        int diseaseId = newDisease.disease.diseaseId;
        DiseaseModifyDTO modifyDisease = createModifyDisease(newDisease.disease, dis_湿疹, adj_急性);
        dbBackendService.modifyDisease(modifyDisease);
        DiseaseFullDTO saved = dbBackend.query(backend -> backend.getDiseaseFull(diseaseId));
        confirm(saved.disease.equals(modifyDisease.disease));
        confirm(saved.adjList.stream().map(full -> full.diseaseAdj.shuushokugocode).collect(toList())
                .equals(List.of(adj_急性)),
                "modifyDisease", () -> System.out.println(saved.adjList));
    }

}
