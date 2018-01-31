package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
}
