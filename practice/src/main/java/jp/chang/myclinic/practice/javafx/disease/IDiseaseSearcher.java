package jp.chang.myclinic.practice.javafx.disease;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IDiseaseSearcher {
    CompletableFuture<List<DiseaseSearchResult>> search(String text, String at);
}
