package jp.chang.myclinic.practice.lib;

import javafx.application.Platform;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.ListSettingDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeLib {

    private static Logger logger = LoggerFactory.getLogger(PracticeLib.class);

    private PracticeLib(){
        throw new AssertionError();
    }

    public static void startPatient(int visitId, PatientDTO patient, Runnable cb) {
        endPatient()
                .thenCompose(result -> {
                    if( visitId == 0 ){
                        return CompletableFuture.completedFuture(null);
                    } else {
                        return Context.frontend.startExam(visitId);
                    }
                })
                .thenAccept(result -> {
                    CompletableFuture<VisitFull2PageDTO> visitsFuture = Context.frontend.listVisitFull2(patient.patientId, 0);
//                    CompletableFuture<List<DiseaseFullDTO>> diseasesFuture = Context.frontend.listCurrentDiseaseFull(patient.patientId);
                    try {
                        VisitFull2PageDTO visits = visitsFuture.join();
//                        List<DiseaseFullDTO> diseases = diseasesFuture.join();
                        PracticeEnv env = PracticeEnv.INSTANCE;
                        Platform.runLater(() -> {
                            env.setCurrentPatient(patient);
                            env.setCurrentVisitId(visitId);
                            env.setTempVisitId(0);
                            env.setTotalRecordPages(visits.totalPages);
                            env.setCurrentRecordPage(visits.page);
                            env.setPageVisits(visits.visits);
//                            env.setCurrentDiseases(diseases);
                            cb.run();
                        });
                    } catch (Exception ex) {
                        logger.error("Failed start patient.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("患者を開始できませんでした。", ex));
                    }
                })
                .exceptionally(HandlerFX::exceptionally);
    }

    public static void startPatient(PatientDTO patient, Runnable cb) {
        startPatient(0, patient, cb);
    }

    public static void startPatient(PatientDTO patient){
        startPatient(patient, () -> {});
    }

    public static CompletableFuture<Void> endPatient(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        if( env.getCurrentPatient() != null ){
            int visitId = env.getCurrentVisitId();
            if( visitId > 0 ){
                return Context.frontend.suspendExam(visitId)
                        .thenAccept(result -> Platform.runLater(PracticeLib::clearCurrentPatient));
            } else {
                clearCurrentPatient();
                return CompletableFuture.completedFuture(null);
            }
        } else {
            return CompletableFuture.completedFuture(null);
        }
    }

    private static void clearCurrentPatient(){
        PracticeEnv env = PracticeEnv.INSTANCE;
        env.setCurrentVisitId(0);
        env.setTempVisitId(0);
        env.setCurrentPatient(null);
        env.setCurrentRecordPage(0);
        env.setTotalRecordPages(0);
        env.setPageVisits(Collections.emptyList());
//        env.setCurrentDiseases(Collections.emptyList());
    }

    public static void endExam(int visitId, int charge, Runnable cb){
        Context.frontend.endExam(visitId, charge)
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
        Context.frontend.enterText(text)
                .thenAccept(textId -> getText(textId, cb))
                .exceptionally(ex -> {
                    logger.error("Failed enter text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の入力に失敗しました。", ex));
                    return null;
                });
    }

    public static void getText(int textId, Consumer<TextDTO> cb) {
        Context.frontend.getText(textId)
                .thenAccept(text -> Platform.runLater(() -> cb.accept(text)))
                .exceptionally(ex -> {
                    logger.error("Failed get text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の取得に失敗しました。", ex));
                    return null;
                });
    }

    public static void updateText(TextDTO newText, Runnable cb) {
        Context.frontend.updateText(newText)
                .thenAccept(result -> Platform.runLater(cb))
                .exceptionally(ex -> {
                    logger.error("Failed update text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の変更に失敗しました。", ex));
                    return null;
                });
    }

    public static void deleteText(TextDTO text, Runnable cb) {
        Context.frontend.deleteText(text.textId)
                .thenAccept(result -> Platform.runLater(cb))
                .exceptionally(ex -> {
                    logger.error("Failed delete text.", ex);
                    Platform.runLater(() -> GuiUtil.alertException("文章の削除に失敗しました。", ex));
                    return null;
                });
    }

//    public static void listWqueue(Consumer<List<WqueueFullDTO>> cb) {
//        Context.frontend.listWqueueFullForExam()
//                .thenAccept(result -> Platform.runLater(() -> cb.accept(result)))
//                .exceptionally(ex -> {
//                    logger.error("Failed list wqueue for exam.", ex);
//                    Platform.runLater(() -> GuiUtil.alertException("受付患者リストの取得に失敗しました。", ex));
//                    return null;
//                });
//    }

    public static void listAvailableHoken(int patientId, String visitedAt, Consumer<HokenDTO> cb) {
        Context.frontend.listAvailableHoken(patientId, LocalDateTime.parse(visitedAt).toLocalDate())
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

    private static CompletableFuture<Void> apiUpdateHoken(VisitDTO visit) {
        return Context.frontend.updateHoken(visit)
                .whenComplete((v, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to update hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報の更新に失敗しました。", ex));
                    }
                });
    }

    private static CompletableFuture<HokenDTO> apiGetHoken(int visitId) {
        return Context.frontend.getHoken(visitId)
                .whenComplete((v, ex) -> {
                    if (ex != null) {
                        logger.error("Failed to get hoken.", ex);
                        Platform.runLater(() -> GuiUtil.alertException("保険情報の取得に失敗しました。", ex));
                    }
                });
    }

    public static Optional<ListSettingDialog> openPrinterSettingList(){
        try {
            PrinterEnv printerEnv = PracticeEnv.INSTANCE.getMyclinicEnv().getPrinterEnv();
            List<String> names = printerEnv.listNames();
            return Optional.of(new ListSettingDialog(names, printerEnv));
        } catch (Exception e) {
            logger.error("Failed to list printer setting names.", e);
            GuiUtil.alertException("印刷設定のリストの取得に失敗しました。", e);
            return Optional.empty();
        }
    }

}
