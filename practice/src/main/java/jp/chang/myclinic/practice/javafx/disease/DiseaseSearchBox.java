package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class DiseaseSearchBox extends SearchBox<DiseaseSearchResult> {

    private DiseaseSearchInputBox inputBox;

    public DiseaseSearchBox(){
        super(s -> CompletableFuture.completedFuture(Collections.emptyList()), DiseaseSearchResult::rep);
    }

    @Override
    protected InputBox createInputBox(){
        inputBox = new DiseaseSearchInputBox();
        inputBox.setOnTextCallback(t -> {
            inputBox.getSearcher().search(t, at)
                    .thenAccept(System.out::println)
                    .exceptionally(HandlerFX::exceptionally);
        });
        return inputBox;
    }
}
