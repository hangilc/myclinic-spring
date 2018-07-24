package jp.chang.myclinic.pharma.tracking;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.pharma.tracking.model.Patient;
import jp.chang.myclinic.pharma.tracking.model.Visit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModelRegistry {

    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private ObservableList<Visit> visitList = FXCollections.observableArrayList();
    private ObservableList<Visit> pharmaList = FXCollections.observableArrayList();

    public ObservableList<Visit> getVisitList(){
        return visitList;
    }

    public ObservableList<Visit> getPharmaList(){
        return pharmaList;
    }

    void createVisit(VisitDTO visitDTO, Runnable toNext) {
        getPatient(visitDTO.patientId)
                .thenAccept(patient -> Platform.runLater(() -> {
                    Visit visit = new Visit(visitDTO.visitId, patient);
                    visitList.add(visit);
                    toNext.run();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private Visit findVisit(int visitId){
        for(Visit visit: visitList){
            if( visit.getVisitId() == visitId ){
                return visit;
            }
        }
        return null;
    }

    void deleteVisit(int visitId) {
        visitList.removeIf(v -> v.getVisitId() == visitId);
        pharmaList.removeIf(v -> v.getVisitId() == visitId);
    }

    void setWqueueWaitState(int visitId, WqueueWaitState waitState){
        Visit visit = findVisit(visitId);
        if( visit != null ){
            visit.setWqueueState(waitState);
        }
    }

    void addToPharmaQueue(int visitId){
        Visit visit = findVisit(visitId);
        if( visit != null ) {
            pharmaList.add(visit);
        }
    }

    void removeFromPharmaQueue(int visitId){
        pharmaList.removeIf(v -> v.getVisitId() == visitId);
    }

    private CompletableFuture<Patient> getPatient(int patientId) {
        Patient patient = patientRegistry.get(patientId);
        if (patient != null) {
            return CompletableFuture.completedFuture(patient);
        } else {
            return Service.api.getPatient(patientId).thenApply(patientDTO -> {
                Patient newPatient = new Patient(patientDTO);
                patientRegistry.put(patientId, newPatient);
                return newPatient;
            });
        }
    }

    void createPatient(PatientDTO patientDTO){
        patientRegistry.put(patientDTO.patientId, new Patient(patientDTO));
    }

    void updatePatient(PatientDTO patientDTO){
        Patient patient = patientRegistry.get(patientDTO.patientId);
        if( patient != null ){
            patient.update(patientDTO);
        }
    }

    void deletePatient(int patientId){
        patientRegistry.remove(patientId);
    }

}
