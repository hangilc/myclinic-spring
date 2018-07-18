package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelRegistry {

    private static Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
    private Service.ServerAPI api;
    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private List<Wqueue> wqueueList = new ArrayList<>();

    public ModelRegistry(Service.ServerAPI api) {
        this.api = api;
    }

    public Visit createVisit(VisitDTO dto){
        Visit visit = new Visit(this, dto);
        visitRegistry.put(dto.visitId, visit);
        return visit;
    }

    public Visit getVisit(int visitId){
        return visitRegistry.get(visitId);
    }

    public void deleteVisit(int visitId){
        visitRegistry.remove(visitId);
    }

    public Wqueue createWqueue(WqueueDTO dto){
        Wqueue wqueue = new Wqueue(this, dto);
        wqueueList.add(wqueue);
        return wqueue;
    }

    public Wqueue findWqueue(int visitId){
        for(Wqueue wqueue : wqueueList){
            if( wqueue.getVisitId() == visitId ){
                return wqueue;
            }
        }
        return null;
    }

    public Wqueue updateWqueue(WqueueDTO dto){
        Wqueue wqueue = findWqueue(dto.visitId);
        if( wqueue != null ){
            wqueue.update(dto);
        }
        return wqueue;
    }

    public boolean deleteWqueue(int visitId){
        return wqueueList.removeIf(wq -> wq.getVisitId() == visitId);
    }

    public Text createText(TextDTO dto){
        Text text = new Text(this, dto);
        Visit visit = getVisit(dto.visitId);
        if( visit != null ){
            visit.getTexts().add(text);
        }
        return text;
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
