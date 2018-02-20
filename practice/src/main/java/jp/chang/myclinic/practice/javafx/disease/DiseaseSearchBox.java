package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.parts.searchbox.SearchBox;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiseaseSearchBox extends
        SearchBox<DiseaseSearchResultModel, DiseaseSearchTextInput, DiseaseSearchResult> {

    public static DiseaseSearchBox create(){
        DiseaseSearchTextInput input = new DiseaseSearchTextInput();
        DiseaseSearchResult result = new DiseaseSearchResult(){
            @Override
            protected CompletableFuture<List<DiseaseSearchResultModel>> doSearch(String text) {
                return null;
            }
        };
        return new DiseaseSearchBox(input, result);
    }

    private DiseaseSearchBox(DiseaseSearchTextInput input, DiseaseSearchResult result){
        super(input, result);
    }

}
