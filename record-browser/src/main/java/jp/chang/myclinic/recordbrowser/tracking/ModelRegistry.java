package jp.chang.myclinic.recordbrowser.tracking;

import javafx.beans.binding.Bindings;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.recordbrowser.tracking.model.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

class ModelRegistry {

    private Map<Integer, Visit> visitRegistry = new HashMap<>();
    private Map<Integer, Patient> patientRegistry = new HashMap<>();
    private Map<Integer, Text> textRegistry = new HashMap<>();
    private Map<Integer, Shahokokuho> shahokokuhoRegistry = new HashMap<>();
    private Map<Integer, Koukikourei> koukikoureiRegistry = new HashMap<>();
    private Map<Integer, Kouhi> kouhiRegistry = new HashMap<>();

    public CompletableFuture<Visit> getVisit(int visitId) {
        if (visitRegistry.containsKey(visitId)) {
            return CompletableFuture.completedFuture(visitRegistry.get(visitId));
        } else {
            class Local {
                VisitDTO visitDTO;
                Patient patient;
                HokenDTO hokenDTO;
                Shahokokuho shahokokuho;
                Koukikourei koukikourei;
                List<Kouhi> kouhiList = new ArrayList<>();
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
                                addShahokokuho(hokenDTO.shahokokuho);
                            }
                        }
                        if (hokenDTO.koukikourei != null) {
                            if (!koukikoureiRegistry.containsKey(hokenDTO.koukikourei.koukikoureiId)) {
                                addKoukikourei(hokenDTO.koukikourei);
                            }
                        }
                        Stream.of(local.hokenDTO.kouhi1, local.hokenDTO.kouhi2, local.hokenDTO.kouhi3)
                                .filter(Objects::nonNull)
                                .forEach(kouhiDTO -> {
                                    if (!kouhiRegistry.containsKey(kouhiDTO.kouhiId)) {
                                        addKouhi(kouhiDTO);
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

    public Text addText(TextDTO textDTO) {
        Text text = new Text(textDTO);
        textRegistry.put(textDTO.textId, text);
        return text;
    }

    public void addShahokokuho(ShahokokuhoDTO dto) {
        Shahokokuho shahokokuho = new Shahokokuho(dto);
        shahokokuhoRegistry.put(dto.shahokokuhoId, shahokokuho);
    }

    public void addKoukikourei(KoukikoureiDTO dto) {
        Koukikourei koukikourei = new Koukikourei(dto);
        koukikoureiRegistry.put(dto.koukikoureiId, koukikourei);
    }

    public void addKouhi(KouhiDTO dto) {
        Kouhi kouhi = new Kouhi(dto);
        kouhiRegistry.put(dto.kouhiId, kouhi);
    }

}
