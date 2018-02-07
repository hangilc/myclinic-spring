package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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

    public static CompletableFuture<IyakuhinMasterDTO> resolveIyakuhinMaster(int iyakuhincode, String at) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        return addExceptionHandler(
                Service.api.resolveIyakuhinMaster(iyakuhincode, at),
                "Failed to resolve iyakuhin master.",
                "医薬品マスターの特定に失敗しました。"
        );
    }

    public static CompletableFuture<IyakuhinMasterDTO> resolveIyakuhinMaster(DrugFullDTO drug, String at) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        return addExceptionHandler(
                Service.api.resolveIyakuhinMaster(drug.drug.iyakuhincode, at),
                "Failed to resolve iyakuhin master.",
                String.format("医薬品マスターの特定に失敗しました：%s", drug.master.name)
        );
    }

    public static void doResolveIyakuhinMaster(int iyakuhincode, String at, Consumer<IyakuhinMasterDTO> cb){
        withCallback(resolveIyakuhinMaster(iyakuhincode, at), cb);
    }

    public static CompletableFuture<List<DrugFullDTO>> listDrugFull(int visitId){
        return addExceptionHandler(
                Service.api.listDrugFull(visitId),
                "Failed to list drugs.",
                "薬剤リストの取得に失敗しました。"
        );
    }

    public static CompletableFuture<Integer> enterDrug(DrugDTO drug){
        return addExceptionHandler(
                Service.api.enterDrug(drug),
                "Failed to enter drug.",
                "新規処方の入力に失敗しました。"
        );
    }

    public static CompletableFuture<DrugFullDTO> getDrugFull(int drugId){
        return addExceptionHandler(
                Service.api.getDrugFull(drugId),
                "Failed to get drug full.",
                "薬剤情報の取得に失敗しました。"
        );
    }

    public static CompletableFuture<VisitDTO> getVisit(int visitId){
        return addExceptionHandler(
                Service.api.getVisit(visitId),
                "Failed to get visit.",
                "診察情報の取得に失敗しました。"
        );
    }

    public static CompletableFuture<Boolean> modifyDrugDays(List<DrugDTO> drugs, int days){
        List<Integer> drugIds = drugs.stream().map(drug -> drug.drugId).collect(Collectors.toList());
        return addExceptionHandler(
                Service.api.batchUpdateDrugDays(drugIds, days),
                "Failed to modify drug days.",
                "処方日数の変更に失敗しました。"
        );
    }


    public static CompletionStage<Boolean> batchDeleteDrugs(List<DrugDTO> drugs) {
        List<Integer> drugIds = drugs.stream().map(drug -> drug.drugId).collect(Collectors.toList());
        return addExceptionHandler(
                Service.api.batchDeleteDrugs(drugIds),
                "Failed to delete drugs.",
                "薬剤の複数削除に失敗しました。"
        );
    }

}
