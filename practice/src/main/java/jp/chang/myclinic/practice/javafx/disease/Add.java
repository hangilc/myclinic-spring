package jp.chang.myclinic.practice.javafx.disease;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseAdjDTO;
import jp.chang.myclinic.dto.DiseaseDTO;
import jp.chang.myclinic.dto.DiseaseNewDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.add.CommandBox;
import jp.chang.myclinic.practice.javafx.disease.add.DiseaseInput;
import jp.chang.myclinic.practice.javafx.disease.add.DiseaseSearchResultModel;
import jp.chang.myclinic.practice.javafx.disease.add.DiseaseSearchTextInput;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchResultList;
import jp.chang.myclinic.practice.lib.PracticeAPI;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Add extends VBox {

    private DiseaseInput diseaseInput;
    private DiseaseSearchTextInput searchTextInput;
    private BasicSearchResultList<DiseaseSearchResultModel> resultList;
    private int patientId;

    public Add(int patientId){
        super(4);
        this.patientId = patientId;
        this.diseaseInput = new DiseaseInput();
        this.searchTextInput = new DiseaseSearchTextInput();
        this.resultList = new BasicSearchResultList<>();
        resultList.setConverter(DiseaseSearchResultModel::rep);
        searchTextInput.setOnSearchCallback(text -> {
            Result<LocalDate, List<String>> result = diseaseInput.getStartDate();
            if( result.hasValue() ){
                searchTextInput.search(text, result.getValue().toString())
                        .thenAccept(resultList::setResult)
                        .exceptionally(HandlerFX::exceptionally);
            } else {
                GuiUtil.alertError("開始日の設定が不適切です。");
            }
        });
        resultList.setOnSelectCallback(model -> model.applyTo(diseaseInput));
        CommandBox commandBox = new CommandBox();
        commandBox.setOnEnterCallback(this::doEnter);
        commandBox.setOnSuspCallback(this::doSusp);
        commandBox.setOnDeleteAdjCallback(this::doDeleteAdj);
        getChildren().addAll(
                diseaseInput,
                commandBox,
                searchTextInput,
                resultList
        );
    }

    private void doEnter(){
        ByoumeiMasterDTO byoumeiMaster = diseaseInput.getByoumei();
        Result<LocalDate, List<String>> startDateResult = diseaseInput.getStartDate();
        if( byoumeiMaster == null ) {
            GuiUtil.alertError("病名が設定されていません。");
        } else if( !startDateResult.hasValue() ){
            GuiUtil.alertError("開始日の設定が不適切です。");
        } else {
            DiseaseDTO disease = new DiseaseDTO();
            disease.patientId = patientId;
            disease.shoubyoumeicode = byoumeiMaster.shoubyoumeicode;
            disease.startDate = startDateResult.getValue().toString();
            disease.endReason = DiseaseEndReason.NotEnded.getCode();
            disease.endDate = "0000-00-00";
            DiseaseNewDTO newDisease = new DiseaseNewDTO();
            newDisease.disease = disease;
            newDisease.adjList = diseaseInput.getShuushokugoList().stream()
                    .map(m -> {
                        DiseaseAdjDTO adj = new DiseaseAdjDTO();
                        adj.shuushokugocode = m.shuushokugocode;
                        return adj;
                    })
                    .collect(Collectors.toList());
            Service.api.enterDisease(newDisease)
                    .thenAccept(Service.api::getDiseaseFull)
                    .thenAccept(entered -> Platform.runLater(() -> {

                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doSusp(){
        PracticeAPI.findDiseaseSusp()
                .thenAccept(adj -> Platform.runLater(() -> {
                    diseaseInput.addShuushokugo(adj);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doDeleteAdj(){
        diseaseInput.clearShuushokugo();
    }

}
