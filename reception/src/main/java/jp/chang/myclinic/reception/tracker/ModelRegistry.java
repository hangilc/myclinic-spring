package jp.chang.myclinic.reception.tracker;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.reception.tracker.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModelRegistry {

    private String today = LocalDate.now().toString();
    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private Map<Integer, Shahokokuho> shahokokuhoRegistry = new HashMap<>();
    private Map<Integer, Koukikourei> koukikoureiRegistry = new HashMap<>();
    private Map<Integer, Kouhi> kouhiRegistry = new HashMap<>();
    private List<Wqueue> wqueueList = new ArrayList<>();
    private List<PharmaQueue> pharmaQueueList = new ArrayList<>();
    private Map<Integer, IyakuhinMasterDTO> iyakuhinMasterRegistry = new HashMap<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterRegistry = new HashMap<>();
    private Map<Integer, KizaiMasterDTO> kizaiMasterRegistry = new HashMap<>();

    public CompletableFuture<Visit> createVisit(VisitDTO visitDTO) {
        class Local {
            private Visit visit;
        }
        Local local = new Local();
        local.visit = new Visit(visitDTO);
        return getPatient(visitDTO.patientId)
                .thenCompose(patient -> {
                    local.visit.setPatient(patient);
                    return updateShahokokuho(local.visit, visitDTO.shahokokuhoId);
                })
                .thenCompose(r -> updateKoukikourei(local.visit, visitDTO.koukikoureiId))
                .thenCompose(r -> updateKouhi(visitDTO.kouhi1Id, local.visit::setKouhi1))
                .thenCompose(r -> updateKouhi(visitDTO.kouhi2Id, local.visit::setKouhi2))
                .thenCompose(r -> updateKouhi(visitDTO.kouhi3Id, local.visit::setKouhi3))
                .thenApply(r -> {
                    Visit visit = local.visit;
                    visit.initHokenRep();
                    visitRegistry.put(visit.getVisitId(), visit);
                    return visit;
                });
    }

    public void deleteVisit(int visitId){
        Visit visit = visitRegistry.get(visitId);
        if( visit != null ){
            visitRegistry.remove(visit);
        }
    }

    public CompletableFuture<Void> updateHoken(Visit visit, VisitDTO updated) {
        return updateShahokokuho(visit, updated.shahokokuhoId)
                .thenCompose(result -> updateKoukikourei(visit, updated.koukikoureiId))
                .thenCompose(result -> updateKouhi(updated.kouhi1Id, visit::setKouhi1))
                .thenCompose(result -> updateKouhi(updated.kouhi2Id, visit::setKouhi2))
                .thenCompose(result -> updateKouhi(updated.kouhi3Id, visit::setKouhi3));
    }

    private CompletableFuture<Void> updateShahokokuho(Visit visit, int shahokokuhoId) {
        if (shahokokuhoId == 0) {
            visit.setShahokokuho(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getShahokokuho(shahokokuhoId)
                    .thenAccept(visit::setShahokokuho);
        }
    }

    private CompletableFuture<Void> updateKoukikourei(Visit visit, int koukikoureiId) {
        if (koukikoureiId == 0) {
            visit.setKoukikourei(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getKoukikourei(koukikoureiId)
                    .thenAccept(visit::setKoukikourei);
        }
    }

    private CompletableFuture<Void> updateKouhi(int kouhiId, Consumer<Kouhi> updater) {
        if (kouhiId == 0) {
            updater.accept(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getKouhi(kouhiId).thenAccept(updater);
        }
    }

    public Visit getVisit(int visitId) {
        return visitRegistry.get(visitId);
    }

    public Visit getCurrentVisit(){
        for(Visit visit: visitRegistry.values()){
            if( visit.getWqueueState() == WqueueWaitState.InExam.getCode() ){
                return visit;
            }
        }
        return null;
    }

    public CompletableFuture<Patient> getPatient(int patientId) {
        if (patientRegistry.containsKey(patientId)) {
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

    public CompletableFuture<Shahokokuho> getShahokokuho(int shahokokuhoId) {
        if (shahokokuhoRegistry.containsKey(shahokokuhoId)) {
            return CompletableFuture.completedFuture(shahokokuhoRegistry.get(shahokokuhoId));
        } else {
            return Service.api.getShahokokuho(shahokokuhoId)
                    .thenApply(shahokokuhoDTO -> {
                        Shahokokuho shahokokuho = new Shahokokuho(shahokokuhoDTO);
                        shahokokuhoRegistry.put(shahokokuhoId, shahokokuho);
                        return shahokokuho;
                    });
        }
    }

    public CompletableFuture<Koukikourei> getKoukikourei(int koukikoureiId) {
        if (koukikoureiRegistry.containsKey(koukikoureiId)) {
            return CompletableFuture.completedFuture(koukikoureiRegistry.get(koukikoureiId));
        } else {
            return Service.api.getKoukikourei(koukikoureiId)
                    .thenApply(koukikoureiDTO -> {
                        Koukikourei koukikourei = new Koukikourei(koukikoureiDTO);
                        koukikoureiRegistry.put(koukikoureiId, koukikourei);
                        return koukikourei;
                    });
        }
    }

    public CompletableFuture<Kouhi> getKouhi(int kouhiId) {
        if (kouhiRegistry.containsKey(kouhiId)) {
            return CompletableFuture.completedFuture(kouhiRegistry.get(kouhiId));
        } else {
            return Service.api.getKouhi(kouhiId)
                    .thenApply(kouhiDTO -> {
                        Kouhi kouhi = new Kouhi(kouhiDTO);
                        kouhiRegistry.put(kouhiId, kouhi);
                        return kouhi;
                    });
        }
    }

    public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode) {
        if (iyakuhinMasterRegistry.containsKey(iyakuhincode)) {
            return CompletableFuture.completedFuture(iyakuhinMasterRegistry.get(iyakuhincode));
        } else {
            return Service.api.resolveIyakuhinMaster(iyakuhincode, today)
                    .thenApply(dto -> {
                        iyakuhinMasterRegistry.put(iyakuhincode, dto);
                        return dto;
                    });
        }
    }

    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode) {
        if (shinryouMasterRegistry.containsKey(shinryoucode)) {
            return CompletableFuture.completedFuture(shinryouMasterRegistry.get(shinryoucode));
        } else {
            return Service.api.resolveShinryouMaster(shinryoucode, today)
                    .thenApply(dto -> {
                        shinryouMasterRegistry.put(shinryoucode, dto);
                        return dto;
                    });
        }
    }

    public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode) {
        if (kizaiMasterRegistry.containsKey(kizaicode)) {
            return CompletableFuture.completedFuture(kizaiMasterRegistry.get(kizaicode));
        } else {
            return Service.api.resolveKizaiMaster(kizaicode, today)
                    .thenApply(dto -> {
                        kizaiMasterRegistry.put(kizaicode, dto);
                        return dto;
                    });
        }
    }

    public Conduct getConduct(int conductId){
        for(Visit visit: visitRegistry.values()){
            for(Conduct conduct: visit.getConducts()){
                if( conduct.getConductId() == conductId ){
                    return conduct;
                }
            }
        }
        return null;
    }

    public Wqueue createWqueue(WqueueDTO dto){
        Wqueue wqueue = new Wqueue(dto, getVisit(dto.visitId));
        wqueueList.add(wqueue);
        return wqueue;
    }

    public void deleteWqueue(int visitId){
        wqueueList.removeIf(item -> item.getVisit().getVisitId() == visitId);
    }

    public Wqueue getWqueue(int visitId){
        for(Wqueue wqueue: wqueueList){
            if( wqueue.getVisit().getVisitId() == visitId ){
                return wqueue;
            }
        }
        return null;
    }

    public PharmaQueue createPharmaQueue(PharmaQueueDTO dto){
        PharmaQueue pharmaQueue = new PharmaQueue(dto, getVisit(dto.visitId), getWqueue(dto.visitId));
        pharmaQueueList.add(pharmaQueue);
        return pharmaQueue;
    }

    public void deletePharmaQueue(int visitId){
        pharmaQueueList.removeIf(item -> item.getVisit().getVisitId() == visitId);
    }

    public PharmaQueue getPharmaQueue(int visitId){
        for(PharmaQueue pharmaQueue: pharmaQueueList){
            if( pharmaQueue.getVisit().getVisitId() == visitId ){
                return pharmaQueue;
            }
        }
        return null;
    }

}
