package jp.chang.myclinic.rcpt;

import jp.chang.myclinic.mastermap2.CodeMapEntry;
import jp.chang.myclinic.mastermap2.MapKind;
import jp.chang.myclinic.mastermap2.MasterMap;
import jp.chang.myclinic.rcpt.resolvedmap2.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class NewCommon2 {

    //private static Logger logger = LoggerFactory.getLogger(NewCommon2.class);

    private NewCommon2() {

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
