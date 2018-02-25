package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeLib {

    private static Logger logger = LoggerFactory.getLogger(PracticeLib.class);

    public static void startPatient(int visitId, PatientDTO patient, Runnable cb) {
        CompletableFuture<VisitFull2PageDTO> visitsFuture = Service.api.listVisitFull2(patient.patientId, 0);
        CompletableFuture<List<DiseaseFullDTO>> diseasesFuture = Service.api.listCurrentDiseaseFull(patient.patientId);
        try {
            VisitFull2PageDTO visits = visitsFuture.join();
            List<DiseaseFullDTO> diseases = diseasesFuture.join();
            PracticeEnv env = PracticeEnv.INSTANCE;
            Platform.runLater(() -> {
                env.setCurrentPatient(patient);
                env.setCurrentVisitId(visitId);
                env.setTempVisitId(0);
                env.setTotalRecordPages(visits.totalPages);
                env.setCurrentRecordPage(visits.page);
                env.setPageVisits(visits.visits);
                env.setCurrentDiseases(diseases);
                cb.run();
            });
        } catch (Exception ex) {
            logger.error("Failed start patient.", ex);
            Platform.runLater(() -> GuiUtil.alertException("患者を開始できませんでした。", ex));
        }
    }

    public static void startPatient(PatientDTO patient, Runnable cb) {
        startPatient(0, patient, cb);
    }

    public static void startPatient(PatientDTO patient){
        startPatient(patient, () -> {});
    }

    private static void clearCurrentPatient(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        env.setCurrentVisitId(0);
        env.setTempVisitId(0);
        env.setCurrentPatient(null);
        env.setCurrentRecordPage(0);
        env.setTotalRecordPages(0);
        env.setPageVisits(Collections.emptyList());
        env.setCurrentDiseases(Collections.emptyList());
    }

    public static void endExam(int visitId, int charge, Runnable cb){
        Service.api.endExam(visitId, charge)
                .thenAccept(result -> Platform.runLater(() -> {
                    clearCurrentPatient();
                    cb.run();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    public static void gotoFirstRecordPage() {
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if (patient != null) {
            int total = env.getTotalRecordPages();
            if (total > 1) {
                int curr = env.getCurrentRecordPage();
                if (curr > 0) {
                    PracticeService.listVisits(patient.patientId, 0, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(0);
                    });
                }
            }
        }
    }

    public static void gotoPrevRecordPage() {
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if (patient != null) {
            int total = env.getTotalRecordPages();
            if (total > 1) {
                int curr = env.getCurrentRecordPage();
                int page = curr - 1;
                if (page >= 0) {
                    PracticeService.listVisits(patient.patientId, page, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(page);
                    });
                }
            }
        }
    }

    public static void gotoNextRecordPage() {
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if (patient != null) {
            int total = env.getTotalRecordPages();
            if (total > 1) {
                int curr = env.getCurrentRecordPage();
                int page = curr + 1;
                if (page < total) {
                    PracticeService.listVisits(patient.patientId, page, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(page);
                    });
                }
            }
        }
    }

    public static void gotoLastRecordPage() {
        PracticeEnv env = PracticeEnv.INSTANCE;
        PatientDTO patient = env.getCurrentPatient();
        if (patient != null) {
            int total = env.getTotalRecordPages();
            if (total > 1) {
                int curr = env.getCurrentRecordPage();
                if (curr < total - 1) {
                    PracticeService.listVisits(patient.patientId, total - 1, visits -> {
                        env.setPageVisits(visits);
                        env.setCurrentRecordPage(total - 1);
                    });
                }
            }
        }
    }

    public static void enterText(int visitId, String content, Consumer<TextDTO> cb) {
        TextDTO text = new TextDTO();
        text.visitId = visitId;
        text.content = content;
        Service.api.enterText(text)
                .thenAccept(textId -> getText(textId, cb))
                .exceptionally(ex -> {
                    logger.error("Failed enter text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の入力に失敗しました。", ex));
                    return null;
                });
    }

    public static void getText(int textId, Consumer<TextDTO> cb) {
        Service.api.getText(textId)
                .thenAccept(text -> Platform.runLater(() -> cb.accept(text)))
                .exceptionally(ex -> {
                    logger.error("Failed get text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の取得に失敗しました。", ex));
                    return null;
                });
    }

    public static void updateText(TextDTO newText, Runnable cb) {
        Service.api.updateText(newText)
                .thenAccept(result -> Platform.runLater(cb))
                .exceptionally(ex -> {
                    logger.error("Failed update text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の変更に失敗しました。", ex));
                    return null;
                });
    }

    public static void deleteText(TextDTO text, Runnable cb) {
        Service.api.deleteText(text.textId)
                .thenAccept(result -> Platform.runLater(cb))
                .exceptionally(ex -> {
                    logger.error("Failed delete text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の削除に失敗しました。", ex));
                    return null;
                });
    }

    public static void listWqueue(Consumer<List<WqueueFullDTO>> cb) {
        Service.api.listWqueueFullForExam()
                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
                .exceptionally(ex -> {
                    logger.error("Failed list wqueue for exam.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("受付患者リストの取得に失敗しました。", ex));
                    return null;
                });
    }

    public static void listAvailableHoken(int patientId, String visitedAt, Consumer<HokenDTO> cb) {
        Service.api.listAvailableHoken(patientId, visitedAt)
                .thenAccept(hoken -> Platform.runLater(() -> cb.accept(hoken)))
                .exceptionally(ex -> {
                    logger.error("Failed list available hoken.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("有効な保険の取得に失敗しました。", ex));
                    return null;
                });
    }

    public static void updateHoken(VisitDTO visit, Consumer<HokenDTO> cb) {
        apiUpdateHoken(visit)
                .thenCompose(result -> apiGetHoken(visit.visitId))
                .thenAccept(hoken -> Platform.runLater(() -> cb.accept(hoken)));
    }

    private static CompletableFuture<Boolean> apiUpdateHoken(VisitDTO visit) {
        return Service.api.updateHoken(visit)
                .whenComplete((v, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to update hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報の更新に失敗しました。", ex));
                    }
                });
    }

    private static CompletableFuture<HokenDTO> apiGetHoken(int visitId) {
        return Service.api.getHoken(visitId)
                .whenComplete((v, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to get hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報の取得に失敗しました。", ex));
                    }
                });
    }

    public static void searchIyakuhinMaster(String text, String at, Consumer<List<IyakuhinMasterDTO>> cb) {
        Service.api.searchIyakuhinMaster(text, at)
                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
                .exceptionally(ex -> {
                    logger.error("Failed search iyakuhin master.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("医薬品マスターの検索に失敗しました。", ex));
                    return null;
                });
    }

    public static void searchPrescExample(String text, Consumer<List<PrescExampleFullDTO>> cb) {
        Service.api.searchPrescExample(text)
                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
                .exceptionally(ex -> {
                    logger.error("Failed search presc example.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("約束処方の検索に失敗しました。", ex));
                    return null;
                });
    }

    public static void searchPreviousPresc(String text, int patientId, Consumer<List<DrugFullDTO>> cb) {
        Service.api.searchPrevDrug(text, patientId)
                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
                .exceptionally(ex -> {
                    logger.error("Failed search previous prescription.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("過去の処方の検索に失敗しました。", ex));
                    return null;
                });
    }

    public static void updateDrug(DrugDTO drug, Consumer<DrugFullDTO> cb){
        PracticeService.updateDrug(drug)
                .thenCompose(result -> PracticeService.getDrugFull(drug.drugId))
                .thenAccept(updatedDrug -> Platform.runLater(() -> cb.accept(updatedDrug)));
    }

    public static void deleteDrug(DrugDTO drug, Runnable cb){
        PracticeService.deleteDrug(drug)
                .thenAccept(result -> Platform.runLater(cb));
    }

    public static void resolveIyakuhinMaster(int iyakuhincode, String at, Consumer<IyakuhinMasterDTO> cb) {
        if (at.length() > 10) {
            at = at.substring(0, 10);
        }
        Service.api.resolveIyakuhinMaster(iyakuhincode, at)
                .thenAccept(master -> Platform.runLater(() -> cb.accept(master)))
                .exceptionally(ex -> {
                    logger.error("Failed resolve iyakuhin master.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("医薬品マスターの特定に失敗しました。", ex));
                    return null;
                });
    }

}
