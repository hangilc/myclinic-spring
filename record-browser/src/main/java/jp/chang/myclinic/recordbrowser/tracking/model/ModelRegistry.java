package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.KizaiUtil;
import jp.chang.myclinic.util.NumberUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModelRegistry {

    private static Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
    private Service.ServerAPI api;
    private ObservableList<RecordModel> recordModels = FXCollections.observableArrayList();
    private Map<Integer, IyakuhinMasterDTO> iyakuhinMasterMap = new HashMap<>();
    private Map<Integer, ShinryouMasterDTO> shinryouMasterMap = new HashMap<>();
    private Map<Integer, KizaiMasterDTO> kizaiMasterMap = new HashMap<>();
    private Map<Integer, ShahokokuhoModel> shahokokuhoMap = new HashMap<>();
    private Map<Integer, KoukikoureiModel> koukikoureiMap = new HashMap<>();
    private Map<Integer, KouhiModel> kouhiMap = new HashMap<>();
    private String today = LocalDate.now().toString();

    public ModelRegistry(Service.ServerAPI api) {
        this.api = api;
    }

    public ObservableList<RecordModel> getRecordModels() {
        return recordModels;
    }

    public void createRecord(VisitDTO visitDTO, Runnable toNext) {
        class Local {
            private PatientModel patientModel;
        }
        Local local = new Local();
        api.getPatient(visitDTO.patientId)
                .thenCompose(patientDTO -> {
                    local.patientModel = new PatientModel(patientDTO);
                    return getHokenModel(visitDTO);
                })
                .thenAccept(hokenModel -> {
                    Platform.runLater(() -> {
                        RecordModel recordModel = new RecordModel(visitDTO, local.patientModel, hokenModel);
                        recordModels.add(recordModel);
                        toNext.run();
                    });
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    public boolean deleteRecord(int visitId) {
        return recordModels.removeIf(r -> r.getVisitId() == visitId);
    }

    public boolean updateWqueue(WqueueDTO wqueueDTO) {
        int visitId = wqueueDTO.visitId;
        for (RecordModel recordModel : recordModels) {
            if (recordModel.getVisitId() == visitId) {
                recordModel.setWaitState(WqueueWaitState.fromCode(wqueueDTO.waitState));
                return true;
            }
        }
        return false;
    }

    private RecordModel findRecordModel(int visitId) {
        for (RecordModel recordModel : recordModels) {
            if (recordModel.getVisitId() == visitId) {
                return recordModel;
            }
        }
        return null;
    }

    public boolean createText(TextDTO textDTO) {
        RecordModel recordModel = findRecordModel(textDTO.visitId);
        if (recordModel != null) {
            TextModel textModel = new TextModel(textDTO);
            recordModel.getTexts().add(textModel);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateText(TextDTO textDTO) {
        RecordModel recordModel = findRecordModel(textDTO.visitId);
        if (recordModel != null) {
            TextModel textModel = recordModel.findTextModel(textDTO.textId);
            if (textModel != null) {
                textModel.setContent(textDTO.content);
                return true;
            } else {
                logger.error("Cannot find text: " + textDTO);
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean deleteText(TextDTO deleted) {
        RecordModel recordModel = findRecordModel(deleted.visitId);
        if (recordModel != null) {
            return recordModel.getTexts().removeIf(t -> t.getTextId() == deleted.textId);
        } else {
            return false;
        }
    }

    public void createDrug(DrugDTO drug, Runnable toNext) {
        RecordModel recordModel = findRecordModel(drug.visitId);
        if (recordModel != null) {
            getIyakuhinMaster(drug.iyakuhincode)
                    .thenAccept(master -> {
                        DrugFullDTO drugFullDTO = new DrugFullDTO();
                        drugFullDTO.drug = drug;
                        drugFullDTO.master = master;
                        String rep = DrugUtil.drugRep(drugFullDTO);
                        DrugModel drugModel = new DrugModel(drug.drugId, rep);
                        Platform.runLater(() -> {
                            recordModel.getDrugs().add(drugModel);
                            toNext.run();
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    public void updateDrug(DrugDTO drug, Consumer<Boolean> cb) {
        RecordModel recordModel = findRecordModel(drug.visitId);
        if (recordModel != null) {
            DrugModel drugModel = recordModel.findDrugModel(drug.drugId);
            if (drugModel != null) {
                getIyakuhinMaster(drug.iyakuhincode)
                        .thenAccept(master -> {
                            DrugFullDTO drugFullDTO = new DrugFullDTO();
                            drugFullDTO.drug = drug;
                            drugFullDTO.master = master;
                            String rep = DrugUtil.drugRep(drugFullDTO);
                            Platform.runLater(() -> {
                                drugModel.setRep(rep);
                                cb.accept(true);
                            });
                        })
                        .exceptionally(HandlerFX::exceptionally);

            } else {
                logger.error("Cannot find drug: " + drug);
                cb.accept(false);
            }
        } else {
            cb.accept(false);
        }
    }

    public boolean deleteDrug(DrugDTO drug) {
        RecordModel recordModel = findRecordModel(drug.visitId);
        if (recordModel != null) {
            return recordModel.getDrugs().removeIf(d -> d.getDrugId() == drug.drugId);
        } else {
            return false;
        }
    }

    public void createShinryou(ShinryouDTO shinryouDTO, Runnable toNext) {
        RecordModel recordModel = findRecordModel(shinryouDTO.visitId);
        if (recordModel != null) {
            getShinryouMaster(shinryouDTO.shinryoucode)
                    .thenAccept(master -> {
                        ShinryouFullDTO drugFullDTO = new ShinryouFullDTO();
                        drugFullDTO.shinryou = shinryouDTO;
                        drugFullDTO.master = master;
                        String rep = master.name;
                        ShinryouModel shinryouModel = new ShinryouModel(shinryouDTO.shinryouId,
                                shinryouDTO.shinryoucode, rep);
                        Platform.runLater(() -> {
                            recordModel.getShinryouList().add(shinryouModel);
                            toNext.run();
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            toNext.run();
        }
    }

    public boolean deleteShinryou(ShinryouDTO shinryouDTO) {
        RecordModel recordModel = findRecordModel(shinryouDTO.visitId);
        if (recordModel != null) {
            return recordModel.getShinryouList().removeIf(s -> s.getShinryouId() == shinryouDTO.shinryouId);
        } else {
            return false;
        }
    }

    public void createConduct(ConductDTO created) {
        RecordModel recordModel = findRecordModel(created.visitId);
        if (recordModel != null) {
            ConductModel conductModel = new ConductModel(created);
            recordModel.getConducts().add(conductModel);
        }
    }

    public boolean updateConduct(ConductDTO updated) {
        ConductModel conductModel = findConductModel(updated.conductId);
        if (conductModel != null) {
            ConductKind conductKind = ConductKind.fromCode(updated.kind);
            if (conductKind != null) {
                conductModel.setConductKindRep(conductKind.getKanjiRep());
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteConduct(ConductDTO deleted) {
        RecordModel recordModel = findRecordModel(deleted.visitId);
        if (recordModel != null) {
            return recordModel.getConducts().removeIf(c -> c.getConductId() == deleted.conductId);
        } else {
            return false;
        }
    }

    private ConductModel findConductModel(int conductId) {
        for (RecordModel recordModel : recordModels) {
            for (ConductModel conductModel : recordModel.getConducts()) {
                if (conductModel.getConductId() == conductId) {
                    return conductModel;
                }
            }
        }
        return null;
    }

    public boolean createGazouLabel(GazouLabelDTO created) {
        ConductModel conductModel = findConductModel(created.conductId);
        if (conductModel != null) {
            conductModel.setGazouLabel(created.label);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateGazouLabel(GazouLabelDTO updated) {
        ConductModel conductModel = findConductModel(updated.conductId);
        if (conductModel != null) {
            conductModel.setGazouLabel(updated.label);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGazouLabel(GazouLabelDTO deleted) {
        ConductModel conductModel = findConductModel(deleted.conductId);
        if (conductModel != null) {
            conductModel.setGazouLabel(null);
            return true;
        } else {
            return false;
        }
    }

    public void createConductShinryou(ConductShinryouDTO created, Consumer<Boolean> altered) {
        ConductModel conductModel = findConductModel(created.conductId);
        if (conductModel != null) {
            getShinryouMaster(created.shinryoucode)
                    .thenAccept(master -> {
                        ConductShinryouModel model = new ConductShinryouModel(created.conductShinryouId, master.name);
                        Platform.runLater(() -> {
                            conductModel.getConductShinryouList().add(model);
                            altered.accept(true);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            altered.accept(false);
        }
    }

    public boolean deleteConductShinryou(ConductShinryouDTO deleted) {
        ConductModel conductModel = findConductModel(deleted.conductId);
        if (conductModel != null) {
            return conductModel.getConductShinryouList().removeIf(m ->
                    m.getConductShinryouId() == deleted.conductShinryouId);
        } else {
            return false;
        }
    }

    public void createConductDrug(ConductDrugDTO created, Consumer<Boolean> altered) {
        ConductModel conductModel = findConductModel(created.conductId);
        if (conductModel != null) {
            getIyakuhinMaster(created.iyakuhincode)
                    .thenAccept(master -> {
                        String rep = DrugUtil.conductDrugRep(created, master);
                        ConductDrugModel model = new ConductDrugModel(created.conductDrugId, rep);
                        Platform.runLater(() -> {
                            conductModel.getConductDrugs().add(model);
                            altered.accept(true);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            altered.accept(false);
        }
    }

    public boolean deleteConductDrug(ConductDrugDTO deleted) {
        ConductModel conductModel = findConductModel(deleted.conductId);
        if (conductModel != null) {
            return conductModel.getConductDrugs().removeIf(m ->
                    m.getConductDrugId() == deleted.conductDrugId);
        } else {
            return false;
        }
    }

    public void createConductKizai(ConductKizaiDTO created, Consumer<Boolean> altered) {
        ConductModel conductModel = findConductModel(created.conductId);
        if (conductModel != null) {
            getKizaiMaster(created.kizaicode)
                    .thenAccept(master -> {
                        String rep = KizaiUtil.kizaiRep(created, master);
                        ConductKizaiModel model = new ConductKizaiModel(created.conductKizaiId, rep);
                        Platform.runLater(() -> {
                            conductModel.getConductKizaiList().add(model);
                            altered.accept(true);
                        });
                    })
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            altered.accept(false);
        }
    }

    public void createShahokokuho(ShahokokuhoDTO created) {
        shahokokuhoMap.put(created.shahokokuhoId, new ShahokokuhoModel(created));
    }

    public void updateShahokokuho(ShahokokuhoDTO updated) {
        ShahokokuhoModel model = shahokokuhoMap.get(updated.shahokokuhoId);
        if (model != null) {
            model.setHokenshaBangou(updated.hokenshaBangou);
            model.setKoureiFutanWari(updated.kourei);
        } else {
            createShahokokuho(updated);
        }
    }

    public void deleteShahokokuho(ShahokokuhoDTO deleted) {
        shahokokuhoMap.remove(deleted.shahokokuhoId);
    }

    public void createKoukikourei(KoukikoureiDTO created) {
        KoukikoureiModel model = new KoukikoureiModel(created);
        koukikoureiMap.put(created.koukikoureiId, model);
    }

    public void updateKoukikourei(KoukikoureiDTO updated) {
        KoukikoureiModel model = koukikoureiMap.get(updated.koukikoureiId);
        if (model != null) {
            model.setFutanWari(updated.futanWari);
        } else {
            createKoukikourei(updated);
        }
    }

    public void deleteKoukikourei(KoukikoureiDTO deleted) {
        koukikoureiMap.remove(deleted.koukikoureiId);
    }

    public void createKouhi(KouhiDTO created) {
        KouhiModel model = new KouhiModel(created);
        kouhiMap.put(created.kouhiId, model);
    }

    public void updateKouhi(KouhiDTO updated) {
        KouhiModel model = kouhiMap.get(updated.kouhiId);
        if (model != null) {
            model.setFutanshaBangou(updated.futansha);
        } else {
            createKouhi(updated);
        }
    }

    public void deleteKouhi(KouhiDTO deleted) {
        kouhiMap.remove(deleted.kouhiId);
    }

    public boolean deleteConductKizai(ConductKizaiDTO deleted) {
        ConductModel conductModel = findConductModel(deleted.conductId);
        if (conductModel != null) {
            return conductModel.getConductKizaiList().removeIf(m ->
                    m.getConductKizaiId() == deleted.conductKizaiId);
        } else {
            return false;
        }
    }

    public void updateHoken(VisitDTO visitDTO, Consumer<Boolean> altered) {
        RecordModel recordModel = findRecordModel(visitDTO.visitId);
        if (recordModel != null) {
            getHokenModel(visitDTO)
                    .thenAccept(hokenModel -> Platform.runLater(() -> {
                        recordModel.updateHoken(hokenModel);
                        altered.accept(true);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            altered.accept(false);
        }
    }

    public boolean createCharge(ChargeDTO created){
        RecordModel recordModel = findRecordModel(created.visitId);
        if( recordModel != null ){
            String rep = String.format("請求額 %s円", NumberUtil.formatNumber(created.charge));
            recordModel.setPaymentRep(rep);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateCharge(ChargeDTO updated){
        RecordModel recordModel = findRecordModel(updated.visitId);
        if( recordModel != null ){
            String rep = String.format("請求額 %s円", NumberUtil.formatNumber(updated.charge));
            recordModel.setPaymentRep(rep);
            return true;
        } else {
            return false;
        }
    }

    public boolean createPayment(PaymentDTO created){
        RecordModel recordModel = findRecordModel(created.visitId);
        if( recordModel != null ){
            String rep = String.format("領収額 %s円", NumberUtil.formatNumber(created.amount));
            recordModel.setPaymentRep(rep);
            return true;
        } else {
            return false;
        }
    }

    private CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode) {
        IyakuhinMasterDTO masterDTO = iyakuhinMasterMap.get(iyakuhincode);
        if (masterDTO != null) {
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveIyakuhinMaster(iyakuhincode, today)
                    .thenApply(result -> {
                        iyakuhinMasterMap.put(iyakuhincode, result);
                        return result;
                    });
        }
    }

    private CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode) {
        ShinryouMasterDTO masterDTO = shinryouMasterMap.get(shinryoucode);
        if (masterDTO != null) {
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveShinryouMaster(shinryoucode, today)
                    .thenApply(result -> {
                        shinryouMasterMap.put(shinryoucode, result);
                        return result;
                    });
        }
    }

    private CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode) {
        KizaiMasterDTO masterDTO = kizaiMasterMap.get(kizaicode);
        if (masterDTO != null) {
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveKizaiMaster(kizaicode, today)
                    .thenApply(result -> {
                        kizaiMasterMap.put(kizaicode, result);
                        return result;
                    });
        }
    }

    private CompletableFuture<ShahokokuhoModel> getShahokokuho(int shahokokuhoId) {
        if (shahokokuhoId == 0) {
            return CompletableFuture.completedFuture(null);
        }
        if (shahokokuhoMap.containsKey(shahokokuhoId)) {
            return CompletableFuture.completedFuture(shahokokuhoMap.get(shahokokuhoId));
        } else {
            return api.getShahokokuho(shahokokuhoId)
                    .thenApply(result -> {
                        ShahokokuhoModel model = new ShahokokuhoModel(result);
                        shahokokuhoMap.put(shahokokuhoId, model);
                        return model;
                    });
        }
    }

    private CompletableFuture<KoukikoureiModel> getKoukikourei(int koukikoureiId) {
        if (koukikoureiId == 0) {
            return CompletableFuture.completedFuture(null);
        }
        if (koukikoureiMap.containsKey(koukikoureiId)) {
            return CompletableFuture.completedFuture(koukikoureiMap.get(koukikoureiId));
        } else {
            return api.getKoukikourei(koukikoureiId)
                    .thenApply(result -> {
                        KoukikoureiModel model = new KoukikoureiModel(result);
                        koukikoureiMap.put(koukikoureiId, model);
                        return model;
                    });
        }
    }

    private CompletableFuture<KouhiModel> getKouhi(int kouhiId) {
        if (kouhiId == 0) {
            return CompletableFuture.completedFuture(null);
        }
        if (kouhiMap.containsKey(kouhiId)) {
            return CompletableFuture.completedFuture(kouhiMap.get(kouhiId));
        } else {
            return api.getKouhi(kouhiId)
                    .thenApply(result -> {
                        KouhiModel model = new KouhiModel(result);
                        kouhiMap.put(kouhiId, model);
                        return model;
                    });
        }
    }

    private CompletableFuture<HokenModel> getHokenModel(VisitDTO visitDTO) {
        HokenModel hokenModel = new HokenModel();
        return getShahokokuho(visitDTO.shahokokuhoId)
                .thenCompose(shahokokuhoModel -> {
                    hokenModel.setShahokokuhoModel(shahokokuhoModel);
                    return getKoukikourei(visitDTO.koukikoureiId);
                })
                .thenCompose(koukikoureiModel -> {
                    hokenModel.setKoukikoureiModel(koukikoureiModel);
                    return getKouhi(visitDTO.kouhi1Id);
                })
                .thenCompose(kouhi1Model -> {
                    hokenModel.setKouhi1Model(kouhi1Model);
                    return getKouhi(visitDTO.kouhi2Id);
                })
                .thenCompose(kouhi2Model -> {
                    hokenModel.setKouhi2Model(kouhi2Model);
                    return getKouhi(visitDTO.kouhi3Id);
                })
                .thenApply(kouhi2Model -> {
                    hokenModel.setKouhi2Model(kouhi2Model);
                    return hokenModel;
                });
    }

}
