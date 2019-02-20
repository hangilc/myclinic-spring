package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

class IntegrationTestHelper {

    private MockData mock = new MockData();

    PatientDTO enterPatient(){
        PatientDTO patient = mock.pickPatient();
        patient.patientId = Service.api.enterPatient(patient).join();
        return patient;
    }

    ShahokokuhoDTO enterShahokokuho(int patientId){
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuho(patientId);
        shahokokuho.shahokokuhoId = Service.api.enterShahokokuho(shahokokuho).join();
        return shahokokuho;
    }

    VisitDTO startVisit(int patientId){
        int visitId = Service.api.startVisit(patientId).join();
        return Service.api.getVisit(visitId).join();
    }

}
