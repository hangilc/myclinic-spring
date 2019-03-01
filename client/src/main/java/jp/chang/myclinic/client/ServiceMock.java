package jp.chang.myclinic.client;

import jp.chang.myclinic.dto.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ServiceMock extends ServiceAdapter {

    private int serialPatientId = 1;
    private Map<Integer, PatientDTO> patientRegistry = new HashMap<>();
    private int serialVisitId = 1;
    private Map<Integer, VisitDTO> visitRegistry = new HashMap<>();

    private Map<Integer, ShahokokuhoDTO> shahokokuhoRegistry = new HashMap<>();
    private Map<Integer, KoukikoureiDTO> koukikoureiRegistry = new HashMap<>();
    private Map<Integer, KouhiDTO> kouhiRegistry = new HashMap<>();
    private Map<Integer, TextDTO> textRegistry = new HashMap<>();
    private Map<Integer, DrugDTO> drugRegistry = new HashMap<>();

    private <T> CompletableFuture<T> future(T t){
        return CompletableFuture.completedFuture(t);
    }

    @Override
    public CompletableFuture<Integer> enterPatient(PatientDTO patient){
        patient.patientId = serialPatientId++;
        patientRegistry.put(patient.patientId, patient);
        return future(patient.patientId);
    }

    @Override
    public CompletableFuture<PatientDTO> getPatient(int patientId) {
        return future(patientRegistry.get(patientId));
    }

    @Override
    public CompletableFuture<Integer> startVisit(int patientId) {
        return super.startVisit(patientId);
    }

}
