package jp.chang.myclinic.reception.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PaymentDTO;
import jp.chang.myclinic.reception.ReceptionEnv;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
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
    
    public static void getMeisaiAndPayments(int visitId, BiConsumer<MeisaiDTO, List<PaymentDTO>> cb){
        CompletableFuture<MeisaiDTO> meisaiFuture = apiGetMeisai(visitId);
        CompletableFuture<List<PaymentDTO>> paymentsFuture = apiListPayment(visitId);
        try {
            MeisaiDTO meisai = meisaiFuture.get();
            List<PaymentDTO> payments = paymentsFuture.get();
            cb.accept(meisai, payments);
        } catch(Exception ex){
            logger.error("Failed to get meisai and payments");
        }
    }

    public static CompletableFuture<MeisaiDTO> apiGetMeisai(int visitId){
        return Service.api.getVisitMeisai(visitId)
                .whenComplete((meisai, ex) -> {
                    if( ex != null ) {
                        logger.error("Failed to get meisai.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("診療明細の取得に失敗しました。", ex));
                    }
                });
    }

    public static CompletableFuture<List<PaymentDTO>> apiListPayment(int visitId){
        return Service.api.listPayment(visitId)
                .whenComplete((payments, ex) -> {
                    if( ex != null ){
                        logger.error("Failed to list payments.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("支払い履歴の取得に失敗しました。", ex));
                    }
                });
    }

    public static void finishCashier(PaymentDTO payment, Runnable cb){
        Service.api.finishCashier(payment)
                .thenAccept(result -> {
                    ReceptionEnv.INSTANCE.getWqueueReloader().trigger();
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
                .thenAccept(result -> {
                    ReceptionEnv.INSTANCE.getWqueueReloader().trigger();
                })
                .exceptionally(ex -> {
                    logger.error("Failed to delete visit from wqueue.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("診察受付の削除に失敗しました。", ex));
                    return null;
                });
    }

}
