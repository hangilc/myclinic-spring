package jp.chang.myclinic.rcpt.resolvedmap;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.mastermap2.MasterNameMap;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class ResolvedDiseaseAdjMap extends ResolvedMapBase {

    @MasterNameMap(candidates="の疑い")
    public int 疑い; // 8002;

    public CompletableFuture<Void> resolveAt(LocalDate at){
        return resolveAt(at, Service.api::batchResolveShuushokugoNames);
    }

    @Override
    public String toString() {
        return "ResolvedDiseaseAdjMap{" +
                "疑い=" + 疑い +
                '}';
    }
}
