package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

class CheckOne {

    private static Logger logger = LoggerFactory.getLogger(CheckOne.class);
    private int patientId;
    private int year;
    private int month;
    private List<String> errs = new ArrayList<>();
    private PatientDTO patient;
    private List<VisitFull2DTO> visits;
    private List<DiseaseFullDTO> diseases;

    CheckOne(int patientId, int year, int month) {
        this.patientId = patientId;
        this.year = year;
        this.month = month;
    }


    private void setPatient(PatientDTO patient){
        this.patient = patient;
        System.out.println("patient: " + patient.lastName + patient.firstName);
    }

    private void setVisits(List<VisitFull2DTO> visits){
        this.visits = visits;
    }

    private void setDiseases(List<DiseaseFullDTO> diseases){
        this.diseases = diseases;
    }

    private CompletableFuture<List<String>> doCheck(){
        return CompletableFuture.completedFuture(errs);
    }

}
