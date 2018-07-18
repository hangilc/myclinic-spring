package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModelRegistry {

    private static Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
    private Service.ServerAPI api;
    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();

    public ModelRegistry(Service.ServerAPI api) {
        this.api = api;
    }

    public Visit createVisit(VisitDTO dto){
        Visit visit = new Visit(this, dto);
        visitRegistry.put(dto.visitId, visit);
        return visit;
    }

    public void deleteVisit(int visitId){
        visitRegistry.remove(visitId);
    }

    public Patient getPatient(int patientId){
        Patient patient = patientRegistry.get(patientId);
        if( patient == null ){
            try {
                PatientDTO dto = api.getPatientCall(patientId).execute().body();
                patient = new Patient(this, dto);
                patientRegistry.put(patientId, patient);
            } catch (IOException e) {
                logger.error("Failed to fetch patient: " + patientId);
            }
        }
        return patient;
    }

}
