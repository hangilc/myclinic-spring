package jp.chang.myclinic.recordbrowser.tracking;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

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
                private Patient patient;
                private HokenDTO hokenDTO;
                private Shahokokuho shahokokuho;
                private Koukikourei koukikourei;
                private List<Kouhi> kouhiList = new ArrayList<>();
            }
            Local local = new Local();
            return Service.api.getVisit(visitId)
                    .thenCompose(visitDTO -> {
                        local.visitDTO = visitDTO;
                        return getPatient(visitDTO.patientId);
                    })
                    .thenCompose(patient -> {
                        local.patient = patient;
                        return Service.api.getHoken(visitId);
                    })
                    .thenApply(hokenDTO -> {
                        local.hokenDTO = hokenDTO;
                        if (hokenDTO.shahokokuho != null) {
                            if (!shahokokuhoRegistry.containsKey(hokenDTO.shahokokuho.shahokokuhoId)) {
                                local.shahokokuho = addShahokokuho(hokenDTO.shahokokuho);
                            }
                        }
                        if (hokenDTO.koukikourei != null) {
                            if (!koukikoureiRegistry.containsKey(hokenDTO.koukikourei.koukikoureiId)) {
                                local.koukikourei = addKoukikourei(hokenDTO.koukikourei);
                            }
                        }
                        Stream.of(local.hokenDTO.kouhi1, local.hokenDTO.kouhi2, local.hokenDTO.kouhi3)
                                .filter(Objects::nonNull)
                                .forEach(kouhiDTO -> {
                                    if (!kouhiRegistry.containsKey(kouhiDTO.kouhiId)) {
                                        local.kouhiList.add(addKouhi(kouhiDTO));
                                    }
                                });
                        return null;
                    })
                    .thenApply(x -> {
                        Visit visit = new Visit(local.visitDTO);
                        visit.setPatient(local.patient);
                        if( local.shahokokuho != null ){
                            visit.setShahokokuho(local.shahokokuho);
                        }
                        if( local.koukikourei != null ){
                            visit.setKoukikourei(local.koukikourei);
                        }
                        if( local.kouhiList.size() >= 1 ){
                            visit.setKouhi1(local.kouhiList.get(0));
                            if( local.kouhiList.size() >= 2 ){
                                visit.setKouhi2(local.kouhiList.get(1));
                                if( local.kouhiList.size() > 3 ){
                                    visit.setKouhi3(local.kouhiList.get(2));
                                }
                            }
                        }
                        visit.initHokenRep();
                        visitRegistry.put(visit.getVisitId(), visit);
                        return visit;
                    });
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

    public Koukikourei addKoukikourei(KoukikoureiDTO dto) {
        Koukikourei koukikourei = new Koukikourei(dto);
        koukikoureiRegistry.put(dto.koukikoureiId, koukikourei);
        return koukikourei;
    }

    public Kouhi addKouhi(KouhiDTO dto) {
        Kouhi kouhi = new Kouhi(dto);
        kouhiRegistry.put(dto.kouhiId, kouhi);
        return kouhi;
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
