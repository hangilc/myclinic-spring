package jp.chang.myclinic.practice.javafx.disease.search;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SelectableList;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SearchBox extends VBox {

    private static Logger logger = LoggerFactory.getLogger(SearchBox.class);
    private Supplier<Result<LocalDate, List<String>>> dateSupplier;
    private DiseaseSearchTextInput textInput;
    private SelectableList<DiseaseSearchResultModel> resultList;

    public SearchBox(Supplier<Result<LocalDate, List<String>>> dateSupplier) {
        super(4);
        this.dateSupplier = dateSupplier;
        this.textInput = new DiseaseSearchTextInput();
        resultList = new SelectableList<>(DiseaseSearchResultModel::rep);
        textInput.setOnSearchCallback(text -> {
            Result<LocalDate, List<String>> resultDate = dateSupplier.get();
            if( resultDate.hasValue() ){
                textInput.getSearcher().search(text, resultDate.getValue().toString())
                        .thenAccept(result -> Platform.runLater(() -> resultList.setList(result)))
                        .exceptionally(HandlerFX::exceptionally);
            } else {
                GuiUtil.alertError("開始日の設定が不適切です。");
            }
        });
        getChildren().addAll(
                textInput,
                resultList
        );
    }

    void triggerByoumeiSelect(ByoumeiMasterDTO master){
        onByoumeiSelect(master);
    }

    public void onByoumeiSelect(ByoumeiMasterDTO master) {

    }

    void triggerShuushokugoSelect(ShuushokugoMasterDTO master){
        onShuushokugoSelect(master);
    }

    public void onShuushokugoSelect(ShuushokugoMasterDTO master) {

    }

    void triggerExampleSelect(DiseaseExampleDTO example){
        Result<LocalDate, List<String>> dateResult = dateSupplier.get();
        if( dateResult.hasValue() ){
            if( example.byoumei != null ){
                Service.api.findByoumeiMasterByName(example.byoumei, dateResult.getValue().toString())
                        .thenAccept(byoumeiMaster -> Platform.runLater(() -> triggerByoumeiSelect(byoumeiMaster)))
                        .exceptionally(HandlerFX::exceptionally);
            }
            CFUtil.forEach(example.adjList, this::handleAdj)
                    .exceptionally(HandlerFX::exceptionally);
        } else {
            GuiUtil.alertError("開始日の設定が不適切です。");
        }
   }

    private CompletableFuture<Void> handleAdj(String name){
        return Service.api.findShuushokugoMasterByName(name)
                .thenAccept(m -> Platform.runLater(() -> triggerShuushokugoSelect(m)));
    }

}
