package jp.chang.myclinic.practice.lib.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ShinryouCopier {

    //private static Logger logger = LoggerFactory.getLogger(ShinryouCopier.class);

    private int targetVisitId;
    private List<ShinryouFullDTO> srcList;
    private BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> onEnterCallback;
    private Consumer<Throwable> errorCallback;
    private Runnable finishedCallback;
    private VisitDTO targetVisit;

    public ShinryouCopier(int targetVisitId, List<ShinryouFullDTO> srcList,
                          BiConsumer<ShinryouFullDTO, ShinryouAttrDTO> cb,
                          Consumer<Throwable> errorHandler, Runnable finishedCallback){
        this.targetVisitId = targetVisitId;
        this.srcList = srcList;
        this.onEnterCallback = cb;
        this.errorCallback = errorHandler;
        this.finishedCallback = finishedCallback;
    }

    public void start(){
        Context.getInstance().getFrontend().getVisit(targetVisitId)
                .thenAccept(targetVisit -> {
                    setTargetVisit(targetVisit);
                    iterate();
                })
                .exceptionally(ex -> {
                    errorCallback.accept(ex);
                    return null;
                });
    }

    private void setTargetVisit(VisitDTO visit){
        this.targetVisit = visit;
    }

    private void iterate(){
        if( srcList.size() == 0 ){
            finishedCallback.run();
        } else {
            ShinryouFullDTO src = srcList.remove(0);
            Context.getInstance().getFrontend().resolveShinryoucode(src.shinryou.shinryoucode, targetVisit.visitedAt)
                    .thenAccept(shinryoucode -> {
                        if( shinryoucode == 0 ){
                            iterate();
                        } else {
                            ShinryouDTO dst = composeShinryou(src.shinryou, shinryoucode);
                            class Local {
                                private ShinryouAttrDTO srcAttr;
                                private ShinryouAttrDTO dstAttr;
                                private int enteredShinryouId;
                            }
                            Local local = new Local();
                            Context.getInstance().getFrontend().getShinryouAttr(src.shinryou.shinryouId)
                                    .thenCompose(srcAttr -> {
                                        local.srcAttr = srcAttr;
                                        return Context.getInstance().getFrontend().enterShinryou(dst);
                                    })
                                    .thenCompose(enteredShinryouId -> {
                                        local.enteredShinryouId = enteredShinryouId;
                                        if( local.srcAttr != null ){
                                            ShinryouAttrDTO dstAttr = ShinryouAttrDTO.copy(local.srcAttr);
                                            dstAttr.shinryouId = local.enteredShinryouId;
                                            local.dstAttr = dstAttr;
                                            return Context.getInstance().getFrontend().enterShinryouAttr(dstAttr);
                                        } else {
                                            local.dstAttr = null;
                                            return CompletableFuture.completedFuture(null);
                                        }
                                    })
                                    .thenCompose(ok -> Context.getInstance().getFrontend().getShinryouFull(local.enteredShinryouId))
                                    .thenAccept(entered -> {
                                        Platform.runLater(() -> onEnterCallback.accept(entered, local.dstAttr));
                                        iterate();
                                    })
                                    .exceptionally(ex -> {
                                        errorCallback.accept(ex);
                                        return null;
                                    });
                        }
                    })
                    .exceptionally(ex -> {
                        errorCallback.accept(ex);
                        return null;
                    });
        }
    }

    private ShinryouDTO composeShinryou(ShinryouDTO src, int shinryoucode){
        ShinryouDTO dst = ShinryouDTO.copy(src);
        dst.visitId = targetVisitId;
        dst.shinryoucode = shinryoucode;
        dst.shinryouId = 0;
        return dst;
    }

}
