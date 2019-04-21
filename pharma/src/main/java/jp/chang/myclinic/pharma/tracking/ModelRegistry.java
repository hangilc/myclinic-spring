package jp.chang.myclinic.pharma.tracking;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.pharma.javafx.ModelImpl;
import jp.chang.myclinic.pharma.javafx.PatientList;
import jp.chang.myclinic.pharma.tracking.model.Patient;
import jp.chang.myclinic.pharma.tracking.model.Visit;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModelRegistry {

    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private ObservableList<PatientList.Model> visitList;
    private ObservableList<PatientList.Model> pharmaList;

    public ModelRegistry(ObservableList<PatientList.Model> visitList, ObservableList<PatientList.Model> pharmaList){
        this.visitList = visitList;
        this.pharmaList = pharmaList;
    }

    void createVisit(VisitDTO visitDTO, Runnable toNext) {
        getPatient(visitDTO.patientId)
                .thenAccept(patient -> Platform.runLater(() -> {
                    Visit visit = new Visit(visitDTO.visitId, patient);
                    visitRegistry.put(visitDTO.visitId, visit);
                    visitList.add(ModelImpl.fromModel(visit));
                    toNext.run();
                }))
                .exceptionally(HandlerFX.exceptionally());
    }

    private Visit findVisit(int visitId){
        return visitRegistry.get(visitId);
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
            pharmaList.add(ModelImpl.fromModel(visit));
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
