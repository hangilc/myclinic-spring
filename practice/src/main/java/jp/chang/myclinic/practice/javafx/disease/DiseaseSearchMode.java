package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public enum DiseaseSearchMode {
    Disease(createDiseaseSearcher()),
    Adj(createAdjSearcher());

    private BiFunction<String, String, CompletableFuture<List<DiseaseSearchResult>>> searcher;

    DiseaseSearchMode(BiFunction<String, String, CompletableFuture<List<DiseaseSearchResult>>> searcher) {
        this.searcher = searcher;
    }

    CompletableFuture<List<DiseaseSearchResult>> search(String text, String at) {
        return searcher.apply(text, at);
    }

    static BiFunction<String, String, CompletableFuture<List<DiseaseSearchResult>>> createDiseaseSearcher() {
        return (t, at) ->
                Service.api.searchByoumei(t, at)
                        .thenApply(result -> result.stream().map(m -> (DiseaseSearchResult) new ByoumeiSearchResult(m))
                                .collect(Collectors.toList()));
    }

    static BiFunction<String, String, CompletableFuture<List<DiseaseSearchResult>>> createAdjSearcher() {
        return (t, at) ->
                Service.api.searchShuushokugo(t)
                        .thenApply(result -> result.stream().map(m -> (DiseaseSearchResult) new ShuushokugoSearchResult(m))
                                .collect(Collectors.toList()));

    }

}
