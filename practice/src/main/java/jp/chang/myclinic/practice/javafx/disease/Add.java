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
import jp.chang.myclinic.practice.javafx.disease.add.*;
import jp.chang.myclinic.practice.javafx.disease.search.DiseaseSearchResultModel;
import jp.chang.myclinic.practice.javafx.disease.search.DiseaseSearchTextInput;
import jp.chang.myclinic.practice.javafx.events.DiseaseEnteredEvent;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchResultList;
import jp.chang.myclinic.practice.lib.PracticeAPI;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Add extends VBox {

    private DiseaseInput diseaseInput;
    private DiseaseSearchTextInput searchTextInput;
    private BasicSearchResultList<DiseaseSearchResultModel> resultList;
    private int patientId;
    private List<DiseaseSearchResultModel> examples;

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
        searchTextInput.setOnExampleCallback(this::doExample);
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
        doExample();
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
                    .thenCompose(Service.api::getDiseaseFull)
                    .thenAccept(entered -> Platform.runLater(() -> {
                        Add.this.fireEvent(new DiseaseEnteredEvent(entered));
                        searchTextInput.clear();
                        resultList.clear();
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

    private void doExample(){
        if( examples != null ){
            resultList.setResult(examples);
        } else {
            Supplier<Result<LocalDate, List<String>>> dateSupplier = () -> diseaseInput.getStartDate();
            Service.api.listDiseaseExample()
                    .thenAccept(examples -> {
                        List<DiseaseSearchResultModel> models = examples.stream()
                                .map(ex -> new ExampleSearchResult(ex, dateSupplier))
                                .collect(Collectors.toList());
                        Add.this.examples = models;
                        resultList.setResult(models);
                    })
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
