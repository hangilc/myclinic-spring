package jp.chang.myclinic.practice.javafx.disease.add;

import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class DiseaseSearchers {

    private static Logger logger = LoggerFactory.getLogger(DiseaseSearchers.class);

    private DiseaseSearchers() {

    }

    public static DiseaseSearcher byoumeiSearcher = (t, at) ->
            Service.api.searchByoumei(t, at)
                    .thenApply(result -> result.stream().map(m -> (DiseaseSearchResultModel) new ByoumeiSearchResult(m))
                            .collect(Collectors.toList()));

    public static DiseaseSearcher shuushokugoSearcher = (t, at) ->
            Service.api.searchShuushokugo(t)
                    .thenApply(result -> result.stream().map(m -> (DiseaseSearchResultModel) new ShuushokugoSearchResult(m))
                            .collect(Collectors.toList()));

}
