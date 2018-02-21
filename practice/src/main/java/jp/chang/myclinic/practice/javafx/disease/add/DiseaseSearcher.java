package jp.chang.myclinic.practice.javafx.disease.add;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiseaseSearcher {
    CompletableFuture<List<DiseaseSearchResultModel>> search(String text, String at);
}