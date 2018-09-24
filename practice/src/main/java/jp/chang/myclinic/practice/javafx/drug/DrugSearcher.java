package jp.chang.myclinic.practice.javafx.drug;

import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class DrugSearcher {

    private static Logger logger = LoggerFactory.getLogger(DrugSearcher.class);

    private DrugSearcher() {

    }

    public static CompletableFuture<List<DrugData>> search(String text, DrugSearchMode mode, LocalDate at){
        switch(mode){
            case Master: return searchMaster(text, at);
            case Example: return searchExample(text, at);
            default: {
                logger.warn("Unknown DrugSearchMode: " + mode);
                return CompletableFuture.completedFuture(Collections.emptyList());
            }
        }
    }

    private static CompletableFuture<List<DrugData>> searchMaster(String text, LocalDate at){
        return Service.api.searchIyakuhinMaster(text, at.toString())
                .thenApply(result -> result.stream().map(DrugData::fromMaster).collect(Collectors.toList()));
    }

    private static CompletableFuture<List<DrugData>> searchExample(String text, LocalDate at){
        return Service.api.searchPrescExample(text)
                .thenApply(result -> result.stream().map(DrugData::fromExample).collect(Collectors.toList()));
    }
}
