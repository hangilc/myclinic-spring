package jp.chang.myclinic.practice.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class CFUtil {

    public static <D> CompletableFuture<Void> forEach(List<D> dataList, Function<D, CompletableFuture<Void>> cvt){
        CompletableFuture<Void> cfMain = new CompletableFuture<>();
        forEachIter(cfMain, dataList, cvt);
        return cfMain;
    }

    private static <D> void forEachIter(CompletableFuture<Void> cfMain, List<D> dataList,
                                          Function<D, CompletableFuture<Void>> cvt){
        if( dataList.size() == 0 ){
            cfMain.complete(null);
        } else {
            D data = dataList.remove(0);
            CompletableFuture<Void> cf = cvt.apply(data);
            cf.whenComplete((result, ex) -> {
                if( ex == null ){
                    forEachIter(cfMain, dataList, cvt);
                } else {
                    cfMain.completeExceptionally(ex);
                }
            });
        }
    }

    public static <D,T> CompletableFuture<List<T>> map(List<D> dataList, Function<D, CompletableFuture<T>> cvt){
        CompletableFuture<List<T>> cfMain = new CompletableFuture<>();
        List<T> accum = new ArrayList<>();
        mapIter(cfMain, accum, dataList, cvt);
        return cfMain;
    }

    private static <D,T> void mapIter(CompletableFuture<List<T>> cfMain, List<T> accum,
                                      List<D> dataList, Function<D, CompletableFuture<T>> cvt){
        if( dataList.size() == 0 ){
            cfMain.complete(accum);
        } else {
            D data = dataList.remove(0);
            CompletableFuture<T> cf = cvt.apply(data);
            cf.whenComplete((result, ex) -> {
                if( ex == null ){
                    accum.add(result);
                    mapIter(cfMain, accum, dataList, cvt);
                } else {
                    cfMain.completeExceptionally(ex);
                }
            });
        }
    }

}
