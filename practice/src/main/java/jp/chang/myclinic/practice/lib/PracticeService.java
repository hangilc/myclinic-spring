package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeService {

    private static Logger logger = LoggerFactory.getLogger(PracticeService.class);

    public static void listRecentVisits(Consumer<List<VisitPatientDTO>> cb){
        Service.api.listRecentVisits()
                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
                .exceptionally(ex -> {
                    logger.error("Failed list recent visits.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("最近の診察の取得に失敗しました。", ex));
                    return null;
                });
    }

    public static void listVisits(int patientId, int page, Consumer<List<VisitFull2DTO>> cb){
        Service.api.listVisitFull2(patientId, page)
                .thenAccept(result -> {
                    Platform.runLater(() -> cb.accept(result.visits));
                })
                .exceptionally(ex -> {
                    logger.error("Failed list visits.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("診察の取得に失敗しました。", ex));
                    return null;
                });
    }

    private static <T> CompletableFuture<T> addExceptionHandler(CompletableFuture<T> cf, String message1, String message2){
        return cf.whenComplete((result, ex) -> {
            if( ex != null ){
                logger.error(message1, ex);
                Platform.runLater(() -> GuiUtil.alertException(message2, ex));
            }
        });
    }

    private static <T> void withCallback(CompletableFuture<T> cf, Consumer<T> cb){
        cf.thenAccept(result -> Platform.runLater(() -> cb.accept(result)));
    }

    public static CompletableFuture<Boolean> deleteVisit(int visitId){
        return addExceptionHandler(
                Service.api.deleteVisit(visitId),
                "Failed to delete visit.",
                "診察の削除に失敗しました。"
        );
    }

    public static void doDeleteVisit(int visitId, Consumer<Boolean> cb){
        withCallback(deleteVisit(visitId), cb);
    }

}
