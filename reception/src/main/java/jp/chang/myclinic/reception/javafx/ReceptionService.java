package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ReceptionService {

    private static Logger logger = LoggerFactory.getLogger(ReceptionService.class);

    public static void startVisit(int patientId){
        Service.api.startVisit(patientId)
                .exceptionally(ex -> {
                    logger.error("Failed start visit.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("診察の受付に失敗しました。", ex));
                    return null;
                });
    }


    public static void finishCashier(PaymentDTO payment, Runnable cb){
        Service.api.finishCashier(payment)
                .thenAccept(result -> {
                    Platform.runLater(() -> cb.run());
                })
                .exceptionally(ex -> {
                    logger.error("Failed finish cashier.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("会計を完了できませんでした。", ex));
                    return null;
                });
    }

    public static void deleteFromWqueue(int visitId){
        Service.api.deleteVisitFromReception(visitId)
                .exceptionally(ex -> {
                    logger.error("Failed to delete visit from wqueue.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("診察受付の削除に失敗しました。", ex));
                    return null;
                });
    }

}
