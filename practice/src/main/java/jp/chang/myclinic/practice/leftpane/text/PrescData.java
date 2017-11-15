package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;

import java.util.concurrent.CompletableFuture;

public class PrescData {

    private TextDTO text;
    private HokenDTO hoken;
    private VisitDTO visit;
    private PatientDTO patient;

    public static CompletableFuture<PrescData> fetch(TextDTO text){
        PrescData data = new PrescData();
        data.text = text;
        return Service.api.getVisit(text.visitId)
                .thenCompose(visit -> {
                    data.visit = visit;
                    return Service.api.getPatient(visit.patientId);
                })
                .thenCompose(patient -> {
                    data.patient = patient;
                    return Service.api.getHoken(text.visitId);
                })
                .thenApply(hoken -> {
                    data.hoken = hoken;
                    return data;
                });
    }

    public TextDTO getText() {
        return text;
    }

    public HokenDTO getHoken() {
        return hoken;
    }

    public VisitDTO getVisit() {
        return visit;
    }

    public PatientDTO getPatient() {
        return patient;
    }

}
