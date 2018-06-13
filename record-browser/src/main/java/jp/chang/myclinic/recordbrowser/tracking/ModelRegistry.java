package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

class ModelRegistry {

    private String today = LocalDate.now().toString();
    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private Map<Integer, Shahokokuho> shahokokuhoRegistry = new HashMap<>();
    private Map<Integer, Koukikourei> koukikoureiRegistry = new HashMap<>();
    private Map<Integer, Kouhi> kouhiRegistry = new HashMap<>();
    private Map<Integer, Drug> drugRegistry = new HashMap<>();
    private Map<Integer, Shinryou> shinryouRegistry = new HashMap<>();
    private Map<Integer, Conduct> conductRegistry = new HashMap<>();
    private Map<Integer, IyakuhinMasterDTO> iyakuhinMasterRegistry = new HashMap<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterRegistry = new HashMap<>();
    private Map<Integer, KizaiMasterDTO> kizaiMasterRegistry = new HashMap<>();

    public CompletableFuture<Visit> createVisit(int visitId) {
        if (visitRegistry.containsKey(visitId)) {
            return CompletableFuture.completedFuture(visitRegistry.get(visitId));
        } else {
            class Local {
                private VisitDTO visitDTO;
                private Visit visit;
            }
            Local local = new Local();
            return Service.api.getVisit(visitId)
                    .thenCompose(visitDTO -> {
                        local.visitDTO = visitDTO;
                        local.visit = new Visit(visitDTO);
                        return getPatient(visitDTO.patientId);
                    })
                    .thenCompose(patient -> {
                        local.visit.setPatient(patient);
                        return updateShahokokuho(local.visit, local.visitDTO.shahokokuhoId);
                    })
                    .thenCompose(r -> updateKoukikourei(local.visit, local.visitDTO.koukikoureiId))
                    .thenCompose(r -> updateKouhi(local.visitDTO.kouhi1Id, local.visit::setKouhi1))
                    .thenCompose(r -> updateKouhi(local.visitDTO.kouhi2Id, local.visit::setKouhi2))
                    .thenCompose(r -> updateKouhi(local.visitDTO.kouhi3Id, local.visit::setKouhi3))
                    .thenApply(r -> {
                        Visit visit = local.visit;
                        visit.initHokenRep();
                        visitRegistry.put(visit.getVisitId(), visit);
                        return visit;
                    });
        }
    }

    public CompletableFuture<Void> updateHoken(Visit visit, VisitDTO updated){
        return updateShahokokuho(visit, updated.shahokokuhoId)
                .thenCompose(result -> updateKoukikourei(visit, updated.koukikoureiId))
                .thenCompose(result -> updateKouhi(updated.kouhi1Id, visit::setKouhi1))
                .thenCompose(result -> updateKouhi(updated.kouhi2Id, visit::setKouhi2))
                .thenCompose(result -> updateKouhi(updated.kouhi3Id, visit::setKouhi3));
    }

    private CompletableFuture<Void> updateShahokokuho(Visit visit, int shahokokuhoId){
        if( shahokokuhoId == 0 ){
            visit.setShahokokuho(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getShahokokuho(shahokokuhoId)
                    .thenAccept(visit::setShahokokuho);
        }
    }

    private CompletableFuture<Void> updateKoukikourei(Visit visit, int koukikoureiId){
        if( koukikoureiId == 0 ){
            visit.setKoukikourei(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getKoukikourei(koukikoureiId)
                    .thenAccept(visit::setKoukikourei);
        }
    }

    private CompletableFuture<Void> updateKouhi(int kouhiId, Consumer<Kouhi> updater){
        if( kouhiId == 0 ){
            updater.accept(null);
            return CompletableFuture.completedFuture(null);
        } else {
            return getKouhi(kouhiId).thenAccept(updater);
        }
    }

    public Visit getVisit(int visitId){
        return visitRegistry.get(visitId);
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

    public Shahokokuho addShahokokuho(ShahokokuhoDTO dto) {
        Shahokokuho shahokokuho = new Shahokokuho(dto);
        shahokokuhoRegistry.put(dto.shahokokuhoId, shahokokuho);
        return shahokokuho;
    }

    public CompletableFuture<Shahokokuho> getShahokokuho(int shahokokuhoId){
        if( shahokokuhoRegistry.containsKey(shahokokuhoId) ){
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

    public Koukikourei addKoukikourei(KoukikoureiDTO dto) {
        Koukikourei koukikourei = new Koukikourei(dto);
        koukikoureiRegistry.put(dto.koukikoureiId, koukikourei);
        return koukikourei;
    }

    public CompletableFuture<Koukikourei> getKoukikourei(int koukikoureiId){
        if( koukikoureiRegistry.containsKey(koukikoureiId) ){
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

    public Kouhi addKouhi(KouhiDTO dto) {
        Kouhi kouhi = new Kouhi(dto);
        kouhiRegistry.put(dto.kouhiId, kouhi);
        return kouhi;
    }

    public CompletableFuture<Kouhi> getKouhi(int kouhiId){
        if( kouhiRegistry.containsKey(kouhiId) ){
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

    public CompletableFuture<Drug> getDrug(DrugDTO drugDTO){
        if( drugRegistry.containsKey(drugDTO.drugId) ){
            return CompletableFuture.completedFuture(drugRegistry.get(drugDTO.drugId));
        } else {
            return getIyakuhinMaster(drugDTO.iyakuhincode)
                    .thenApply(master -> {
                        Drug drug = new Drug(drugDTO, master);
                        drugRegistry.put(drugDTO.drugId, drug);
                        return drug;
                    });
        }
    }

    public CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode){
        if( iyakuhinMasterRegistry.containsKey(iyakuhincode) ){
            return CompletableFuture.completedFuture(iyakuhinMasterRegistry.get(iyakuhincode));
        } else {
            return Service.api.resolveIyakuhinMaster(iyakuhincode, today)
                    .thenApply(dto -> {
                        iyakuhinMasterRegistry.put(iyakuhincode, dto);
                        return dto;
                    });
        }
    }

    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode){
        if( shinryouMasterRegistry.containsKey(shinryoucode) ){
            return CompletableFuture.completedFuture(shinryouMasterRegistry.get(shinryoucode));
        } else {
            return Service.api.resolveShinryouMaster(shinryoucode, today)
                    .thenApply(dto -> {
                        shinryouMasterRegistry.put(shinryoucode, dto);
                        return dto;
                    });
        }
    }

    public CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode){
        if( kizaiMasterRegistry.containsKey(kizaicode) ){
            return CompletableFuture.completedFuture(kizaiMasterRegistry.get(kizaicode));
        } else {
            return Service.api.resolveKizaiMaster(kizaicode, today)
                    .thenApply(dto -> {
                        kizaiMasterRegistry.put(kizaicode, dto);
                        return dto;
                    });
        }
    }

    public CompletableFuture<Shinryou> getShinryou(ShinryouDTO shinryouDTO){
        if( shinryouRegistry.containsKey(shinryouDTO.shinryouId) ){
            return CompletableFuture.completedFuture(shinryouRegistry.get(shinryouDTO.shinryouId));
        } else {
            return getShinryouMaster(shinryouDTO.shinryoucode)
                    .thenApply(master -> {
                        Shinryou shinryou = new Shinryou(shinryouDTO, master);
                        shinryouRegistry.put(shinryouDTO.shinryouId, shinryou);
                        return shinryou;
                    });
        }
    }

    public Conduct createConduct(ConductDTO conductDTO){
        Conduct conduct = new Conduct(conductDTO);
        conductRegistry.put(conductDTO.conductId, conduct);
        return conduct;
    }

    public Conduct getConduct(int conductId){
        return conductRegistry.get(conductId);
    }

}
