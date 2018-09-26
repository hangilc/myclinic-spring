package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Searcher {

    //private static Logger logger = LoggerFactory.getLogger(Searcher.class);

    private Searcher() {
    }

    public static CompletableFuture<List<SearchResultItem>> searchMaster(String text, LocalDate at,
                                                                         Consumer<IyakuhinMasterDTO> onSelectHandler) {
        Service.api.searchIyakuhinMaster(text, at.toString())
                .thenApply();
    }

}

