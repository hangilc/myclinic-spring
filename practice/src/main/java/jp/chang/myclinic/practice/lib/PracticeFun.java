package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.dto.BatchEnterResultDTO;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class PracticeFun {

    public interface CallbackInvoker {
        void invoke(Runnable cb);
    }

    private CallbackInvoker invoker;

    public PracticeFun(CallbackInvoker invoker){
        this.invoker = invoker;
    }

    private void invoke(Runnable cb){
        invoker.invoke(cb);
    }

    private static class BatchEnterShinryouByNamesStore {
        BatchEnterResultDTO result;
        List<ShinryouFullDTO> shinryouList;
    }

    public void batchEnterShinryouByNames(List<String> names, int visitId,
                                                 BiConsumer<List<ShinryouFullDTO>, List<ConductFullDTO>> cb){
        BatchEnterShinryouByNamesStore store = new BatchEnterShinryouByNamesStore();
        PracticeService.batchEnterShinryouByName(names, visitId)
                .thenCompose(result -> {
                    store.result = result;
                    return PracticeService.listShinryouFullByIds(result.shinryouIds);
                })
                .thenCompose(shinryouList -> {
                    store.shinryouList = shinryouList;
                    if( store.result.conductIds.size() == 0 ){
                        return CompletableFuture.completedFuture(Collections.emptyList());
                    } else {
                        return PracticeService.listConductFullByIds(store.result.conductIds);
                    }
                })
                .thenAccept(conductList -> invoke(() -> cb.accept(store.shinryouList, conductList)));
    }

}
