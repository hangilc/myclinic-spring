package jp.chang.myclinic.practice.testgui;

import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.mockdata.MockData;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestFramework {
//
//    private BackendAsync restService;
//    private MockData mock = new MockData();
//
//    public TestFramework(BackendAsync restService){
//        this.restService = restService;
//    }
//
//    public PatientDTO enterPatient(){
//        PatientDTO patient = mock.pickPatient();
//        patient.patientId = restService.enterPatient(patient).join();
//        return patient;
//    }
//
//    public VisitFull2DTO startVisit(int patientId, LocalDateTime at){
//        VisitDTO visit = restService.startVisit(patientId, at).join();
//        VisitFull2DTO full = new VisitFull2DTO();
//        full.visit = visit;
//        full.texts = new ArrayList<>();
//        full.hoken = restService.getHoken(visit.visitId).join();
//        full.drugs = new ArrayList<>();
//        full.shinryouList = new ArrayList<>();
//        full.conducts = new ArrayList<>();
//        return full;
//    }
//
//    public TextDTO addText(VisitFull2DTO full, String content){
//        TextDTO text = new TextDTO();
//        text.visitId = full.visit.visitId;
//        text.content = content;
//        text.textId = restService.enterText(text).join();
//        full.texts.add(text);
//        return text;
//    }
}
