package jp.chang.myclinic.practice.lib.shinryou;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class ShinryouCopier {

    private static Logger logger = LoggerFactory.getLogger(ShinryouCopier.class);

    private int targetVisitId;
    private List<ShinryouFullDTO> srcList;
    private Consumer<ShinryouFullDTO> onEnterCallback;
    private Consumer<String> errorCallback;
    private Runnable finishedCallback;
    private VisitDTO targetVisit;

    public ShinryouCopier(int targetVisitId, List<ShinryouFullDTO> srcList, Consumer<ShinryouFullDTO> cb,
                          Consumer<String> errorHandler, Runnable finishedCallback){
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
                    logger.error("Failed get visit.", ex);
                    errorCallback.accept("コピー先診療行為の情報の取得に失敗しました。");
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
                                    .thenAccept(shinryouId -> {

                                    })
                                    .exceptionally(ex -> {
                                        logger.error("Failed to enter shinryou.", ex);
                                        errorCallback.accept("診療行為の入力に失敗しました。");
                                        return null;
                                    });
                        }
                    })
                    .exceptionally(ex -> {
                        logger.error("Failed to resolve shinryoucode.", ex);
                        errorCallback.accept("診療行為が有効でありません。");
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
