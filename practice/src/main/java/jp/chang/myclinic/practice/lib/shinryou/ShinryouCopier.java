package jp.chang.myclinic.practice.lib.shinryou;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.client.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class ShinryouCopier {

    private static Logger logger = LoggerFactory.getLogger(ShinryouCopier.class);

    private int targetVisitId;
    private List<ShinryouFullDTO> srcList;
    private Consumer<ShinryouFullDTO> onEnterCallback;
    private Consumer<Throwable> errorCallback;
    private Runnable finishedCallback;
    private VisitDTO targetVisit;

    public ShinryouCopier(int targetVisitId, List<ShinryouFullDTO> srcList, Consumer<ShinryouFullDTO> cb,
                          Consumer<Throwable> errorHandler, Runnable finishedCallback){
        this.targetVisitId = targetVisitId;
        this.srcList = srcList;
        this.onEnterCallback = cb;
        this.errorCallback = errorHandler;
        this.finishedCallback = finishedCallback;
    }

    public void start(){
        Service.api.getVisit(targetVisitId)
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
            Service.api.resolveShinryoucode(src.shinryou.shinryoucode, targetVisit.visitedAt)
                    .thenAccept(shinryoucode -> {
                        if( shinryoucode == 0 ){
                            iterate();
                        } else {
                            ShinryouDTO dst = composeShinryou(src.shinryou, shinryoucode);
                            Service.api.enterShinryou(dst)
                                    .thenCompose(Service.api::getShinryouFull)
                                    .thenAccept(entered -> {
                                        onEnterCallback.accept(entered);
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
