package jp.chang.myclinic.practice.lib;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeAPI {

    private static Logger logger = LoggerFactory.getLogger(PracticeAPI.class);

    private Consumer<String> errorHandler;

    public PracticeAPI(Consumer<String> errorHandler){
        this.errorHandler = errorHandler;
    }

    private <T> CompletableFuture<T> addExceptionHandler(CompletableFuture<T> cf, String message1, String message2){
        return cf.whenComplete((result, ex) -> {
            if( ex != null ){
                logger.error(message1, ex);
                errorHandler.accept(message2);
            }
        });
    }

    public CompletableFuture<VisitDTO> getVisit(int visitId){
        return addExceptionHandler(
                Service.api.getVisit(visitId),
                "Failed to get visit.",
                "診察記録の取得に失敗しました。"
        );
    }

    public CompletableFuture<Integer> resolveShinryoucode(int shinryoucode, String at){
        return addExceptionHandler(
                Service.api.resolveShinryoucode(shinryoucode, at),
                "Failed to resolve shinryoucode.",
                "診療行為の有効情報の取得に失敗しました。"
        );
    }

    public CompletableFuture<ShinryouMasterDTO> getShinryouMaster(int shinryoucode, String at){
        return addExceptionHandler(
                Service.api.getShinryouMaster(shinryoucode, at),
                "Failed to get shinryou master.",
                "診療マスターの取得に失敗しました。"
        );
    }

    private class ShinryouCopier {
        private VisitDTO visit;
        private List<ShinryouFullDTO> srcList;
        private Consumer<ShinryouFullDTO> enteredCallback;
        private Runnable finishedCallback;

        ShinryouCopier(VisitDTO visit, List<ShinryouFullDTO> srcList, Consumer<ShinryouFullDTO> enteredCallback,
                       Runnable finishedCallback){
            this.visit = visit;
            this.srcList = srcList;
            this.enteredCallback = enteredCallback;
            this.finishedCallback = finishedCallback;
        }

        void iterate(){
            if( srcList.size() == 0 ){
                finishedCallback.run();
            } else {
                ShinryouFullDTO src = srcList.remove(0);
                resolveShinryoucode(src.shinryou.shinryoucode, visit.visitedAt)
                        .thenAccept(shinryoucode -> {
                            if( shinryoucode == 0 ){
                                iterate();
                            } else {

                            }
                        });
            }
        }

    }

    public CompletableFuture<List<ShinryouFullDTO>> copyShinryou(int targetVisitId, List<ShinryouFullDTO> srcList,
                                                                 Consumer<ShinryouFullDTO> enteredCallback,
                                                                 Runnable finishedCallback){
        getVisit(targetVisitId)
                .thenAccept(visit -> {
                    ShinryouCopier copier = new ShinryouCopier(visit, srcList, enteredCallback, finishedCallback);
                    copier.iterate();
                });
        return null;
    }

}
