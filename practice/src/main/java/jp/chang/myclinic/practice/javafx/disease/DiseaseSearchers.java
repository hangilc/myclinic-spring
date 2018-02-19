package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class DiseaseSearchers {

    private static Logger logger = LoggerFactory.getLogger(DiseaseSearchers.class);

    private DiseaseSearchers() {

    }

    public static IDiseaseSearcher byoumeiSearcher = (t, at) ->
            Service.api.searchByoumei(t, at)
                    .thenApply(result -> result.stream().map(m -> (DiseaseSearchResult) new ByoumeiSearchResult(m))
                            .collect(Collectors.toList()));

    public static IDiseaseSearcher shuushokugoSearcher = (t, at) ->
            Service.api.searchShuushokugo(t)
                    .thenApply(result -> result.stream().map(m -> (DiseaseSearchResult) new ShuushokugoSearchResult(m))
                            .collect(Collectors.toList()));

}
