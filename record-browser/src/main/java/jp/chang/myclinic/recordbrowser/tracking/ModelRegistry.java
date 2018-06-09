package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.recordbrowser.tracking.model.Patient;
import jp.chang.myclinic.recordbrowser.tracking.model.Text;
import jp.chang.myclinic.recordbrowser.tracking.model.Visit;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class ModelRegistry {

    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private Map<Integer, Text> textRegistry = new HashMap<>();

    public CompletableFuture<Visit> getVisit(int visitId){
        if( visitRegistry.containsKey(visitId) ){
            return CompletableFuture.completedFuture(visitRegistry.get(visitId));
        } else {
            class Local { Visit visit; }
            Local local = new Local();
            return Service.api.getVisit(visitId)
                    .thenCompose(visitDTO -> {
                        local.visit = new Visit(visitDTO);
                        return getPatient(visitDTO.patientId);
                    })
                    .thenApply(patient -> {
                        local.visit.setPatient(patient);
                        visitRegistry.put(visitId, local.visit);
                        return local.visit;
                    });
        }
    }

    public CompletableFuture<Patient> getPatient(int patientId){
        if( patientRegistry.containsKey(patientId) ){
            return CompletableFuture.completedFuture(patientRegistry.get(patientId));
        } else {
            return Service.api.getPatient(patientId)
                    .thenApply(patientDTO -> {
                        Patient patient = new Patient(patientDTO);
                        patientRegistry.put(patientId, patient);
                        return patient;
                    });
        }
    }

    public Text addText(TextDTO textDTO){
        Text text = new Text(textDTO);
        textRegistry.put(textDTO.textId, text);
        return text;
    }

}
