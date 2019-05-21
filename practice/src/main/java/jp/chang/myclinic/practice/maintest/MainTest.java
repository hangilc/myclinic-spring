package jp.chang.myclinic.practice.maintest;

import javafx.application.Platform;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Tester;
import jp.chang.myclinic.practice.javafx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MainTest implements Tester.TestTarget, MainTestMixin {

    private MainPane mainPane;

    public MainTest(MainPane mainPane) {
        this.mainPane = mainPane;
    }

    @Override
    public List<Tester.TestMethod> listTestMethods() {
        return List.of(
                new Tester.TestMethod("disp", this::disp),
                new Tester.TestMethod("finishExam", this::finishExam),
                new Tester.TestMethod("searchPatientById", this::searchPatientById),
                new Tester.TestMethod("searchPatientInNewVisit", this::searchByPatientIdInNewVisitDialog),
                new Tester.TestMethod("kouhatsuKasan", this::kouhatsuKasan)
        );
    }

    private static class ExamEnv {
        int patientId;
        VisitDTO visit;
        Record record;
    }

    private CompletableFuture<Void> _startExam(CompletableFuture<Void> pre, ExamEnv env) {
        Frontend frontend = Context.frontend;
        if (env.patientId == 0) {
            env.patientId = 1;
        }
        return pre.thenComposeAsync(ignore -> frontend.startVisit(env.patientId, LocalDateTime.now()))
                .thenAcceptAsync(visit -> {
                    env.visit = visit;
                    Platform.runLater(() -> mainPane.simulateSelectVisitMenuChoice());
                })
                .thenApplyAsync(ignore -> waitForWindow(SelectFromWqueueDialog.class))
                .thenAcceptAsync(dialog -> {
                    dialog.simulateSelectVisit(env.visit.visitId);
                    dialog.simulateSelectButtonClick();
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> {
                    env.record = waitFor(() -> mainPane.findRecord(env.visit.visitId));
                });
    }

    private CompletableFuture<Void> _endExam(ExamEnv env) {
        Frontend frontend = Context.frontend;
        return CompletableFuture.completedFuture(null)
                .thenApplyAsync(ignore -> waitFor(() -> mainPane.findPatientManip()))
                .thenAcceptAsync(manip ->
                        Platform.runLater(manip::simulateClickCashierButton))
                .thenApplyAsync(ignore -> waitForWindow(CashierDialog.class))
                .thenAcceptAsync(CashierDialog::simulateClickEnterButton, Platform::runLater)
                .thenComposeAsync(ignore -> {
                    waitForFail(() -> mainPane.findRecord(env.visit.visitId));
                    return frontend.deleteWqueue(env.visit.visitId);
                })
                .thenAcceptAsync(ignore -> frontend.deletePharmaQueue(env.visit.visitId));
    }

    private CompletableFuture<Void> disp(CompletableFuture<Void> pre) {
        return pre;
    }

    private CompletableFuture<Void> kouhatsuKasan(CompletableFuture<Void> pre) {
        ExamEnv eEnv = new ExamEnv();
        return _startExam(pre, eEnv)
                .thenAcceptAsync(ignore -> {
                    eEnv.record.simulateAddRegularShinryouClick();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitFor(eEnv.record::findAddRegularForm))
                .thenAcceptAsync(form -> {
                    form.simulateSelectItem("外来後発医薬品使用体制加算２");
                    form.simulateClickEnterButton();
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> {
                    waitForTrue(() -> {
                        for (RecordShinryou shinryou : eEnv.record.listShinryou()) {
                            if (shinryou.getShinryoucode() == 120004070) {
                                return true;
                            }
                        }
                        return false;
                    });
                })
                .thenComposeAsync(ignore -> _endExam(eEnv));
    }

    private CompletableFuture<Void> searchByPatientIdInNewVisitDialog(CompletableFuture<Void> pre) {
        return pre
                .thenAccept(ignore -> {
                    Platform.runLater(() -> mainPane.simulateNewVisitMenuChoice());
                })
                .thenApplyAsync(ignore -> waitForWindow(NewVisitDialog.class))
                .thenApplyAsync(dialog -> {
                    dialog.simulateSearchTextFocus();
                    dialog.simulateSearchTextInsert("1");
                    dialog.simulateSearchButtonClick();
                    return dialog;
                }, Platform::runLater)
                .thenApplyAsync(dialog -> {
                    waitForTrue(() -> {
                        List<PatientDTO> results = dialog.getSearchResults();
                        return results.size() == 1 && results.get(0).patientId == 1;
                    });
                    return dialog;
                })
                .thenAcceptAsync(Stage::close, Platform::runLater);
    }

    private CompletableFuture<Void> searchPatientById(CompletableFuture<Void> pre) {
        return pre.thenAcceptAsync(ignore -> {
            mainPane.simulateSearchPatientMenuChoice();
        }, Platform::runLater)
                .thenApplyAsync(ignore -> {
                    return waitForWindow(SearchPatientDialog.class);
                })
                .thenAcceptAsync(dialog -> {
                    dialog.simulateSetSearchText("1");
                    dialog.simulateSearchButtonClick();
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> waitForTrue(() -> {
                    PatientDTO patient = Context.currentPatientService.getCurrentPatient();
                    return patient != null && patient.patientId == 1;
                }))
                .thenAcceptAsync(ignore ->
                                Context.currentPatientService.setCurrentPatient(null, 0),
                        Platform::runLater);
    }

    private CompletableFuture<Void> finishExam(CompletableFuture<Void> pre) {
        Frontend frontend = Context.frontend;
        class Local {
            private VisitDTO visit;
        }
        Local local = new Local();
        return pre.thenComposeAsync(ignore -> frontend.startVisit(1, LocalDateTime.now()))
                .thenAcceptAsync(visit -> {
                    local.visit = visit;
                    mainPane.simulateSelectVisitMenuChoice();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> {
                    return waitForWindow(SelectFromWqueueDialog.class);
                })
                .thenAcceptAsync(dialog -> {
                    dialog.simulateSelectVisit(local.visit.visitId);
                    dialog.simulateSelectButtonClick();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> {
                    waitFor(() -> mainPane.findRecord(local.visit.visitId));
                    return waitFor(() -> mainPane.findPatientManip());
                })
                .thenAcceptAsync(PatientManip::simulateClickCashierButton, Platform::runLater)
                .thenApplyAsync(ignore -> waitForWindow(CashierDialog.class))
                .thenAcceptAsync(CashierDialog::simulateClickEnterButton, Platform::runLater)
                .thenAcceptAsync(ignore -> {
                    waitForFail(() -> mainPane.findRecord(local.visit.visitId));
                });
    }

}
