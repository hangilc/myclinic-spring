package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Optional;

class IntegrationTestHelper {

    private MockData mock = new MockData();

    PatientDTO enterPatient(){
        PatientDTO patient = mock.pickPatient();
        patient.patientId = Context.frontend.enterPatient(patient).join();
        return patient;
    }

    ShahokokuhoDTO enterShahokokuho(int patientId){
        ShahokokuhoDTO shahokokuho = mock.pickShahokokuho(patientId);
        shahokokuho.shahokokuhoId = Context.frontend.enterShahokokuho(shahokokuho).join();
        return shahokokuho;
    }

    VisitDTO startVisit(int patientId){
        return Context.frontend.startVisit(patientId, LocalDateTime.now()).join();
    }

}
