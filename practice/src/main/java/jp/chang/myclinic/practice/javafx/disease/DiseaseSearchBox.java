package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.searchbox.SearchBox;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DiseaseSearchBox extends
        SearchBox<DiseaseSearchResultModel, DiseaseSearchTextInput, DiseaseSearchResult> {

    public static DiseaseSearchBox create(Supplier<Result<LocalDate, List<String>>> dateSupplier){
        DiseaseSearchTextInput input = new DiseaseSearchTextInput();
        DiseaseSearchResult result = new DiseaseSearchResult(){
            @Override
            protected CompletableFuture<List<DiseaseSearchResultModel>> doSearch(String text) {
                Result<LocalDate, List<String>> result = dateSupplier.get();
                if( result.hasValue() ){
                    return input.getSearcher().search(text, result.getValue().toString());
                } else {
                    GuiUtil.alertError("開始日の設定が不適切です。");
                    return CompletableFuture.completedFuture(Collections.emptyList());
                }
            }
        };
        return new DiseaseSearchBox(input, result);
    }

    private DiseaseSearchBox(DiseaseSearchTextInput input, DiseaseSearchResult result){
        super(input, result);
    }

}
