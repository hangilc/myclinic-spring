package jp.chang.myclinic.practice.javafx.disease.add;

import javafx.application.Platform;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.search.DiseaseSearchResultModel;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ExampleSearchResult implements DiseaseSearchResultModel {

    private static Logger logger = LoggerFactory.getLogger(ExampleSearchResult.class);
    private DiseaseExampleDTO example;
    private Supplier<Result<LocalDate, List<String>>> dateSupplier;

    public ExampleSearchResult(DiseaseExampleDTO example, Supplier<Result<LocalDate, List<String>>> dateSupplier) {
        this.example = example;
        this.dateSupplier = dateSupplier;
    }

    @Override
    public String rep() {
        return example.label;
    }

    @Override
    public void applyTo(DiseaseInput diseaseInput) {
        Result<LocalDate, List<String>> dateResult = dateSupplier.get();
        if( dateResult.hasValue() ){
            if( example.byoumei != null ){
                Service.api.findByoumeiMasterByName(example.byoumei, dateResult.getValue().toString())
                        .thenAccept(byoumeiMaster -> Platform.runLater(() -> diseaseInput.setByoumei(byoumeiMaster)))
                        .exceptionally(HandlerFX::exceptionally);
            }
            CFUtil.forEach(example.adjList, name -> handleAdj(name, diseaseInput))
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            GuiUtil.alertError("開始日の設定が不適切です。");
        }
    }

    private CompletableFuture<Void> handleAdj(String name, DiseaseInput diseaseInput){
        return Service.api.findShuushokugoMasterByName(name)
                .thenAccept(m -> Platform.runLater(() -> diseaseInput.addShuushokugo(m)));
    }

}
