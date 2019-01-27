package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.rcpt.resolvedmap.*;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class Common {

    //private static Logger logger = LoggerFactory.getLogger(NewCommon2.class);

    private Common() {

    }

    public static CompletableFuture<ResolvedMap> getMasterMaps(LocalDate at){
        ResolvedMap resolvedMap = new ResolvedMap();
        ResolvedShinryouMap resolvedShinryouMap = new ResolvedShinryouMap();
        ResolvedKizaiMap resolvedKizaiMap = new ResolvedKizaiMap();
        ResolvedDiseaseMap resolvedDiseaseMap = new ResolvedDiseaseMap();
        ResolvedDiseaseAdjMap resolvedDiseaseAdjMap = new ResolvedDiseaseAdjMap();
        return resolvedShinryouMap.resolveAt(at)
                .thenCompose(v -> {
                    resolvedMap.shinryouMap = resolvedShinryouMap;
                    return resolvedKizaiMap.resolveAt(at);
                })
                .thenCompose(v -> {
                    resolvedMap.kizaiMap = resolvedKizaiMap;
                    return resolvedDiseaseMap.resolveAt(at);
                })
                .thenCompose(v -> {
                    resolvedMap.diseaseMap = resolvedDiseaseMap;
                    return resolvedDiseaseAdjMap.resolveAt(at);
                })
                .thenApply(v -> {
                    resolvedMap.diseaseAdjMap = resolvedDiseaseAdjMap;
                    return resolvedMap;
                });
    }
}
