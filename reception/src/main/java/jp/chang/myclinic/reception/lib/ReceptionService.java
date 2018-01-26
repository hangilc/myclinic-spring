package jp.chang.myclinic.reception.lib;

import javafx.application.Platform;
import jp.chang.myclinic.reception.ReceptionEnv;
import jp.chang.myclinic.reception.Service;
import jp.chang.myclinic.reception.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class ReceptionService {

    private static Logger logger = LoggerFactory.getLogger(ReceptionService.class);

    public static void startVisit(int patientId){
        startVisit(patientId, visitId -> {});
    }

    public static void startVisit(int patientId, Consumer<Integer> cb){
        Service.api.startVisit(patientId)
                .thenAccept(visitId -> {
                    Platform.runLater(() -> {
                        ReceptionEnv.INSTANCE.getWqueueReloader().trigger();
                        cb.accept(visitId);
                    });
                })
                .exceptionally(ex -> {
                    logger.error("Failed start visit.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("診察の受付に失敗しました。", ex));
                    return null;
                });
    }

}
