package jp.chang.myclinic.practice.javafx.disease;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DiseaseEndReason;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.add.CommandBox;
import jp.chang.myclinic.practice.javafx.disease.add.DiseaseInput;
import jp.chang.myclinic.practice.javafx.disease.search.DiseaseSearchResultModel;
import jp.chang.myclinic.practice.javafx.disease.search.ExampleSearchResult;
import jp.chang.myclinic.practice.javafx.disease.search.SearchBox;
import jp.chang.myclinic.practice.lib.PracticeAPI;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// TODO: 「糖尿病の疑い」を入力して、もう一度選択すると「糖尿病」となる
public class Add extends VBox {

    private DiseaseInput diseaseInput;
    private SearchBox searchBox;
    private int patientId;
    private List<DiseaseSearchResultModel> examples;

    protected Add(int patientId){
        super(4);
        getStyleClass().add("add-disease-pane");
        this.patientId = patientId;
        this.diseaseInput = new DiseaseInput();
        this.searchBox = new SearchBox(() -> diseaseInput.getStartDate()){
            @Override
            protected void onByoumeiSelect(ByoumeiMasterDTO master) {
                diseaseInput.setByoumei(master);
            }

            @Override
            protected void onShuushokugoSelect(ShuushokugoMasterDTO master) {
                diseaseInput.addShuushokugo(master);
            }

            @Override
            protected void onExample() {
                doExample();
                clearTextInput();
            }
        };
        CommandBox commandBox = new CommandBox();
        commandBox.setOnEnterCallback(this::doEnter);
        commandBox.setOnSuspCallback(this::doSusp);
        commandBox.setOnDeleteAdjCallback(this::doDeleteAdj);
        getChildren().addAll(
                diseaseInput,
                commandBox,
                searchBox
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
            Context.getInstance().getFrontend().enterDisease(newDisease)
                    .thenCompose(Context.getInstance().getFrontend()::getDiseaseFull)
                    .thenAccept(entered -> Platform.runLater(() -> {
                        onEntered(entered);
                        diseaseInput.clear();
                        searchBox.clearTextInput();
                        doExample();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    protected void onEntered(DiseaseFullDTO entered){

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
            searchBox.setList(examples);
        } else {
            Supplier<Result<LocalDate, List<String>>> dateSupplier = () -> diseaseInput.getStartDate();
            Context.getInstance().getFrontend().listDiseaseExample()
                    .thenAccept(examples -> Platform.runLater(() -> {
                        List<DiseaseSearchResultModel> models = examples.stream()
                                .map(ex -> new ExampleSearchResult(ex, dateSupplier))
                                .collect(Collectors.toList());
                        Add.this.examples = models;
                        searchBox.setList(models);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
