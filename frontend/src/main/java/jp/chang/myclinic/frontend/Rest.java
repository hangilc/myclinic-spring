package jp.chang.myclinic.frontend;

import jp.chang.myclinic.dto.PatientDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Rest {

    public static void main(String[] args){
        FrontendRest rest = new FrontendRest();
        System.out.println(rest.getPatient(1).join());
        rest.testVoid().thenAccept(ignore -> System.out.println("void success"));
        List<PatientDTO> patients = new ArrayList<>();
        PatientDTO patient = new PatientDTO();
        patient.patientId = 10;
        patient.lastName = "田中";
        patient.firstName = "隆";
        patients.add(patient);
        patient = new PatientDTO();
        patient.patientId = 11;
        patient.lastName = "鈴木";
        patient.firstName = "須磨子";
        patients.add(patient);
        System.out.println(rest.testEcho(patients).join());
        FrontendRest.executorService.shutdown();
    }
}
