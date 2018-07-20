package jp.chang.myclinic.recordbrowser.tracking.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.WqueueWaitState;
import jp.chang.myclinic.dto.TextDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.util.HokenUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelRegistry {

    private static Logger logger = LoggerFactory.getLogger(ModelRegistry.class);
    private Service.ServerAPI api;
    private ObservableList<RecordModel> recordModels = FXCollections.observableArrayList();

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

}
