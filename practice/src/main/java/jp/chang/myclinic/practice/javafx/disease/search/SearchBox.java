package jp.chang.myclinic.practice.javafx.disease.search;

import javafx.application.Platform;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ByoumeiMasterDTO;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.dto.ShuushokugoMasterDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
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

    protected SearchBox(Supplier<Result<LocalDate, List<String>>> dateSupplier) {
        super(4);
        this.dateSupplier = dateSupplier;
        this.textInput = new DiseaseSearchTextInput();
        resultList = new SelectableList<>(DiseaseSearchResultModel::rep);
        resultList.getStyleClass().add("search-result");
        resultList.setOnSelectCallback(m -> m.onSelect(this));
        textInput.setOnSearchCallback(text -> {
            Result<LocalDate, List<String>> resultDate = dateSupplier.get();
            if( resultDate.hasValue() ){
                textInput.getSearcher().search(text, resultDate.getValue().toString())
                        .thenAccept(result -> Platform.runLater(() -> {
                            if( result.size() > 0 ){
                                DiseaseSearchResultModel model = result.get(0);
                                if( model instanceof ShuushokugoSearchResult ){
                                    resultList.setSingleResultAutoSelect(false);
                                } else {
                                    resultList.setSingleResultAutoSelect(true);
                                }
                            }
                            resultList.setList(result);
                        }))
                        .exceptionally(HandlerFX.exceptionally(this));
            } else {
                GuiUtil.alertError("開始日の設定が不適切です。");
            }
        });
        textInput.setOnExampleCallback(this::onExample);
        getChildren().addAll(
                textInput,
                resultList
        );
    }

    public void clear(){
        textInput.clear();
        resultList.clear();
    }

    public void clearTextInput(){
        textInput.clear();
    }

    public void setList(List<DiseaseSearchResultModel> list){
        resultList.setList(list);
    }

    void triggerByoumeiSelect(ByoumeiMasterDTO master){
        onByoumeiSelect(master);
    }

    protected void onExample(){

    }

    protected void onByoumeiSelect(ByoumeiMasterDTO master) {

    }

    void triggerShuushokugoSelect(ShuushokugoMasterDTO master){
        onShuushokugoSelect(master);
    }

    protected void onShuushokugoSelect(ShuushokugoMasterDTO master) {

    }

    void triggerExampleSelect(DiseaseExampleDTO example){
        Result<LocalDate, List<String>> dateResult = dateSupplier.get();
        if( dateResult.hasValue() ){
            if( example.byoumei != null ){
                Context.frontend.getByoumeiMasterByName(example.byoumei, dateResult.getValue())
                        .thenAccept(byoumeiMaster -> Platform.runLater(() -> triggerByoumeiSelect(byoumeiMaster)))
                        .exceptionally(HandlerFX.exceptionally(this));
            }
            CFUtil.forEach(example.adjList, this::handleAdj)
                    .exceptionally(HandlerFX.exceptionally(this));
        } else {
            GuiUtil.alertError("開始日の設定が不適切です。");
        }
   }

    private CompletableFuture<Void> handleAdj(String name){
        return Context.frontend.getShuushokugoMasterByName(name)
                .thenAccept(m -> Platform.runLater(() -> triggerShuushokugoSelect(m)));
    }

}
