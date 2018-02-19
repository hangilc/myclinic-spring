package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.parts.SearchBox;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class DiseaseSearchBox extends SearchBox<DiseaseSearchResult> {

    public DiseaseSearchBox(){
        super(s -> CompletableFuture.completedFuture(Collections.emptyList()), DiseaseSearchResult::rep);
    }

    @Override
    protected InputBox createInputBox(){
        return new DiseaseSearchInputBox();
    }
}
