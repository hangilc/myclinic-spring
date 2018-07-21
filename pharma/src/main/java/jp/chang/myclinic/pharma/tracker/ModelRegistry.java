package jp.chang.myclinic.pharma.tracker;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.pharma.tracker.model.Patient;
import jp.chang.myclinic.pharma.tracker.model.Visit;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

class ModelRegistry {

    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();

    CompletableFuture<Visit> createVisit(VisitDTO visitDTO) {
        return getPatient(visitDTO.patientId)
                .thenApply(patient -> {
                    Visit visit = new Visit(visitDTO.visitId, patient);
                    visitRegistry.put(visitDTO.visitId, visit);
                    return visit;
                });
    }

    void deleteVisit(int visitId){
        visitRegistry.remove(visitId);
    }

    Visit getVisit(int visitId){
        return visitRegistry.get(visitId);
    }

    private CompletableFuture<Patient> getPatient(int patientId){
        Patient patient = patientRegistry.get(patientId);
        if( patient != null ){
            return CompletableFuture.completedFuture(patient);
        } else {
            return Service.api.getPatient(patientId).thenApply(patientDTO -> {
                Patient newPatient = new Patient(patientDTO);
                patientRegistry.put(patientId, newPatient);
                return newPatient;
            });
        }
    }
}
