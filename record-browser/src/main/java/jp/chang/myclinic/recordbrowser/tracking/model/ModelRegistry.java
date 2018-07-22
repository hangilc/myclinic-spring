package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DrugUtil;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.util.KizaiUtil;
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
    private String today = LocalDate.now().toString();

    public ModelRegistry(Service.ServerAPI api) {
        this.api = api;
    }

    public ObservableList<RecordModel> getRecordModels(){
        return recordModels;
    }

    public void createRecord(VisitDTO visitDTO, Runnable toNext){
        class Local {
            private PatientModel patientModel;
        }
        Local local = new Local();
        api.getPatient(visitDTO.patientId)
                .thenCompose(patientDTO -> {
                    local.patientModel = new PatientModel(patientDTO);
                    return api.convertToHoken(visitDTO);
                })
                .thenAccept(hokenDTO -> {
                    String hokenRep = HokenUtil.hokenRep(hokenDTO);
                    RecordModel recordModel = new RecordModel(visitDTO, local.patientModel, hokenRep);
                    Platform.runLater(() -> {
                        recordModels.add(recordModel);
                        toNext.run();
                    });
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    public boolean deleteRecord(int visitId){
        return recordModels.removeIf(r -> r.getVisitId() == visitId);
    }

    public boolean updateWqueue(WqueueDTO wqueueDTO){
        int visitId = wqueueDTO.visitId;
        for(RecordModel recordModel: recordModels){
            if( recordModel.getVisitId() == visitId ){
                recordModel.setWaitState(WqueueWaitState.fromCode(wqueueDTO.waitState));
                return true;
            }
        }
        return false;
    }

    private RecordModel findRecordModel(int visitId){
        for(RecordModel recordModel: recordModels){
            if( recordModel.getVisitId() == visitId ){
                return recordModel;
            }
        }
        return null;
    }

    public boolean createText(TextDTO textDTO){
        RecordModel recordModel = findRecordModel(textDTO.visitId);
        if( recordModel != null ){
            TextModel textModel = new TextModel(textDTO);
            recordModel.getTexts().add(textModel);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateText(TextDTO textDTO){
        RecordModel recordModel = findRecordModel(textDTO.visitId);
        if( recordModel != null ){
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

    public boolean deleteText(TextDTO deleted){
        RecordModel recordModel = findRecordModel(deleted.visitId);
        if( recordModel != null ){
            return recordModel.getTexts().removeIf(t -> t.getTextId() == deleted.textId);
        } else {
            return false;
        }
    }

    public void createDrug(DrugDTO drug, Runnable toNext){
        RecordModel recordModel = findRecordModel(drug.visitId);
        if( recordModel != null ) {
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

    public void updateDrug(DrugDTO drug, Consumer<Boolean> cb){
        RecordModel recordModel = findRecordModel(drug.visitId);
        if( recordModel != null ){
            DrugModel drugModel = recordModel.findDrugModel(drug.drugId);
            if( drugModel != null ){
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

    public boolean deleteDrug(DrugDTO drug){
        RecordModel recordModel = findRecordModel(drug.visitId);
        if( recordModel != null ){
            return recordModel.getDrugs().removeIf(d -> d.getDrugId() == drug.drugId);
        } else {
            return false;
        }
    }

    public void createShinryou(ShinryouDTO shinryouDTO, Runnable toNext){
        RecordModel recordModel = findRecordModel(shinryouDTO.visitId);
        if( recordModel != null ) {
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

    public boolean deleteShinryou(ShinryouDTO shinryouDTO){
        RecordModel recordModel = findRecordModel(shinryouDTO.visitId);
        if( recordModel != null ){
            return recordModel.getShinryouList().removeIf(s -> s.getShinryouId() == shinryouDTO.shinryouId);
        } else {
            return false;
        }
    }

    public void createConduct(ConductDTO created){
        RecordModel recordModel = findRecordModel(created.visitId);
        if( recordModel != null ) {
            ConductModel conductModel = new ConductModel(created);
            recordModel.getConducts().add(conductModel);
        }
    }

    public boolean updateConduct(ConductDTO updated){
        ConductModel conductModel = findConductModel(updated.conductId);
        if( conductModel != null ){
            ConductKind conductKind = ConductKind.fromCode(updated.kind);
            conductModel.setConductKindRep(conductKind.getKanjiRep());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteConduct(ConductDTO deleted){
        RecordModel recordModel = findRecordModel(deleted.visitId);
        if( recordModel != null ){
            return recordModel.getConducts().removeIf(c -> c.getConductId() == deleted.conductId);
        } else {
            return false;
        }
    }

    private ConductModel findConductModel(int conductId){
        for(RecordModel recordModel: recordModels){
            for(ConductModel conductModel: recordModel.getConducts()){
                if( conductModel.getConductId() == conductId ){
                    return conductModel;
                }
            }
        }
        return null;
    }

    public boolean createGazouLabel(GazouLabelDTO created){
        ConductModel conductModel = findConductModel(created.conductId);
        if( conductModel != null ){
            conductModel.setGazouLabel(created.label);
            return true;
        } else {
            return false;
        }
    }

    public boolean updateGazouLabel(GazouLabelDTO updated){
        ConductModel conductModel = findConductModel(updated.conductId);
        if( conductModel != null ){
            conductModel.setGazouLabel(updated.label);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteGazouLabel(GazouLabelDTO deleted){
        ConductModel conductModel = findConductModel(deleted.conductId);
        if( conductModel != null ){
            conductModel.setGazouLabel(null);
            return true;
        } else {
            return false;
        }
    }

    public void createConductShinryou(ConductShinryouDTO created, Consumer<Boolean> altered){
        ConductModel conductModel = findConductModel(created.conductId);
        if( conductModel != null ) {
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

    public boolean deleteConductShinryou(ConductShinryouDTO deleted){
        ConductModel conductModel = findConductModel(deleted.conductId);
        if( conductModel != null ){
            return conductModel.getConductShinryouList().removeIf(m ->
                    m.getConductShinryouId() == deleted.conductShinryouId);
        } else {
            return false;
        }
    }

    public void createConductDrug(ConductDrugDTO created, Consumer<Boolean> altered){
        ConductModel conductModel = findConductModel(created.conductId);
        if( conductModel != null ) {
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

    public boolean deleteConductDrug(ConductDrugDTO deleted){
        ConductModel conductModel = findConductModel(deleted.conductId);
        if( conductModel != null ){
            return conductModel.getConductDrugs().removeIf(m ->
                    m.getConductDrugId() == deleted.conductDrugId);
        } else {
            return false;
        }
    }

    public void createConductKizai(ConductKizaiDTO created, Consumer<Boolean> altered){
        ConductModel conductModel = findConductModel(created.conductId);
        if( conductModel != null ) {
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

    public boolean deleteConductKizai(ConductKizaiDTO deleted){
        ConductModel conductModel = findConductModel(deleted.conductId);
        if( conductModel != null ){
            return conductModel.getConductKizaiList().removeIf(m ->
                    m.getConductKizaiId() == deleted.conductKizaiId);
        } else {
            return false;
        }
    }

    private CompletableFuture<IyakuhinMasterDTO> getIyakuhinMaster(int iyakuhincode){
        IyakuhinMasterDTO masterDTO = iyakuhinMasterMap.get(iyakuhincode);
        if( masterDTO != null ){
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveIyakuhinMaster(iyakuhincode, today)
                    .thenApply(result -> {
                        iyakuhinMasterMap.put(iyakuhincode, result);
                        return result;
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode){
        ShinryouMasterDTO masterDTO = shinryouMasterMap.get(shinryoucode);
        if( masterDTO != null ){
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveShinryouMaster(shinryoucode, today)
                    .thenApply(result -> {
                        shinryouMasterMap.put(shinryoucode, result);
                        return result;
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private CompletableFuture<KizaiMasterDTO> getKizaiMaster(int kizaicode){
        KizaiMasterDTO masterDTO = kizaiMasterMap.get(kizaicode);
        if( masterDTO != null ){
            return CompletableFuture.completedFuture(masterDTO);
        } else {
            return api.resolveKizaiMaster(kizaicode, today)
                    .thenApply(result -> {
                        kizaiMasterMap.put(kizaicode, result);
                        return result;
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
