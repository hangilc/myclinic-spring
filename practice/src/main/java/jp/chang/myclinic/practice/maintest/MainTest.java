package jp.chang.myclinic.practice.maintest;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Border;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.Tester;
import jp.chang.myclinic.practice.javafx.*;
import jp.chang.myclinic.practice.javafx.shinryou.AddKensaForm;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.CompletionStageRxInvoker;
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
                new Tester.TestMethod("finishExam", this::finishExam)
                , new Tester.TestMethod("searchPatientById", this::searchPatientById)
                , new Tester.TestMethod("searchPatientInNewVisit", this::searchByPatientIdInNewVisitDialog)
                , new Tester.TestMethod("kouhatsuKasan", this::kouhatsuKasan)
                , new Tester.TestMethod("kouhatsuKasanAutoCheck", this::kouhatsuKasanAutoCheck)
                , new Tester.TestMethod("recordsHeightAfterReopen", this::recordsHeightAfterReopen)
                , new Tester.TestMethod("cancelKensaInput", this::cancelKensaInput)
        );
    }

    @Override
    public List<Tester.TestMethod> listSingleTestMethods() {
        return List.of(
                new Tester.TestMethod("disp", this::disp)
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

    private CompletableFuture<Void> _endVisit(ExamEnv env) {
        Frontend frontend = Context.frontend;
        return CompletableFuture.supplyAsync(() -> waitFor(mainPane::findPatientManip))
                .thenAcceptAsync(PatientManip::simulateEndPatientClick, Platform::runLater)
                .thenAcceptAsync(ignore -> {
                    waitForFail(() -> mainPane.findRecord(env.visit.visitId));
                    frontend.deleteWqueue(env.visit.visitId);
                })
                .thenAcceptAsync(ignore -> frontend.deletePharmaQueue(env.visit.visitId));
    }

    private CompletableFuture<Void> invokeFromSelectPatientMenu(String itemText) {
        return CompletableFuture.supplyAsync(() -> {
            Menu menu = mainPane.getSelectPatientMenu();
            for (MenuItem item : menu.getItems()) {
                if (item.getText().equals(itemText)) {
                    item.fire();
                    break;
                }
            }
            return null;
        }, Platform::runLater);
    }

    private CompletableFuture<Void> disp(CompletableFuture<Void> pre) {
        return pre
                .thenAcceptAsync(ignore -> {
                    Context.practiceConfig.setKouhatsuKasan("外来後発医薬品使用体制加算２");
                });
    }

    private CompletableFuture<Void> cancelKensaInput(CompletableFuture<Void> pre) {
        ExamEnv env = new ExamEnv();
        class Local {
            private ShinryouMenu menu;
        }
        Local local = new Local();
        return _startExam(pre, env)
                .thenAcceptAsync(ignore -> {
                    RecordShinryouPane shinryouPane = env.record.getShinryouPane();
                    ShinryouMenu menu = shinryouPane.getMenu();
                    local.menu = menu;
                    menu.simulateAuxLinkClick();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitForWindow(ContextMenu.class))
                .thenAcceptAsync(menu -> {
                    for (MenuItem item : menu.getItems()) {
                        if (item.getText().equals("検査")) {
                            item.fire();
                            menu.hide();
                            return;
                        }
                    }
                    throw new RuntimeException("Cannot find context menu item: 検査");
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitFor(local.menu::findAddKensaForm))
                .thenAcceptAsync(AddKensaForm::simulateCancelClick, Platform::runLater)
                .thenAcceptAsync(ignore -> waitForFail(local.menu::findAddKensaForm))
                .thenAcceptAsync(ignore -> _endExam(env));

    }

    private CompletableFuture<Void> recordsHeightAfterReopen(CompletableFuture<Void> pre) {
        ExamEnv env = new ExamEnv();
        class Local {
            private ShinryouMenu menu;
            private double firstHeight;
        }
        Local local = new Local();
        return _startExam(pre, env)
                .thenAcceptAsync(ignore -> {
                    RecordShinryouPane shinryouPane = env.record.getShinryouPane();
                    ShinryouMenu menu = shinryouPane.getMenu();
                    local.menu = menu;
                    menu.simulateAuxLinkClick();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitForWindow(ContextMenu.class))
                .thenAcceptAsync(menu -> {
                    for (MenuItem item : menu.getItems()) {
                        if (item.getText().equals("検査")) {
                            item.fire();
                            menu.hide();
                            return;
                        }
                    }
                    throw new RuntimeException("Cannot find context menu item: 検査");
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitFor(local.menu::findAddKensaForm))
                .thenAcceptAsync(form -> {
                    form.simulateSetKensaSelect();
                    form.simulateEnterClick();
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> waitForFail(local.menu::findAddKensaForm))
                .thenAcceptAsync(ignore -> {
                    double height = mainPane.getRecordsPane().getHeight();
                    local.firstHeight = height;
                    PatientManip manip = mainPane.findPatientManip().orElseThrow(() -> {
                        throw new RuntimeException("Cannot find patient manip.");
                    });
                    manip.simulateEndPatientClick();
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> waitForFail(() -> mainPane.findRecord(env.visit.visitId)))
                .thenComposeAsync(ignore -> {
                    RecordsPane recordsPane = mainPane.getRecordsPane();
                    double height = recordsPane.getHeight();
                    double expected = 0.0;
                    Border border = recordsPane.getBorder();
                    if (border != null) {
                        expected += border.getOutsets().getTop() + border.getOutsets().getBottom() +
                                border.getInsets().getTop() + border.getInsets().getBottom();
                    }
                    confirm(height == expected, () -> System.out.println("Empty records has height: " + height));
                    return invokeFromSelectPatientMenu("最近の診察");
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitForWindow(RecentVisitsDialog.class))
                .thenAcceptAsync(dialog -> {
                    dialog.simulateItemSelect(env.patientId);
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> waitFor(() -> mainPane.findRecord(env.visit.visitId)))
                .thenAcceptAsync(ignore -> {
                    double height = mainPane.getRecordsPane().getHeight();
                    confirm(height == local.firstHeight);
                }, Platform::runLater)
                .thenAcceptAsync(ignore -> _endVisit(env));
    }

    private CompletableFuture<Void> kouhatsuKasanAutoCheck(CompletableFuture<Void> pre) {
        ExamEnv env = new ExamEnv();
        class Local {
            private String kouhatsuKasanSave;
        }
        Local local = new Local();
        return _startExam(pre, env)
                .thenAcceptAsync(ignore -> {
                    local.kouhatsuKasanSave = Context.practiceConfig.getKouhatsuKasan();
                    Context.practiceConfig.setKouhatsuKasan("外来後発医薬品使用体制加算２");
                    env.record.simulateAddRegularShinryouClick();
                }, Platform::runLater)
                .thenApplyAsync(ignore -> waitFor(env.record::findAddRegularForm))
                .thenApplyAsync(form -> {
                    form.simulateSelectItem("処方料");
                    return form;
                }, Platform::runLater)
                .thenComposeAsync(form -> {
                    waitForTrue(() ->
                            form.isItemChecked("外来後発医薬品使用体制加算２"));
                    return _endExam(env);
                });
    }

    private CompletableFuture<Void> kouhatsuKasan(CompletableFuture<Void> pre) {
        ExamEnv eEnv = new ExamEnv();
        class Local {
            String kouhatsuKasanSave;
        }
        Local local = new Local();
        return _startExam(pre, eEnv)
                .thenAcceptAsync(ignore -> {
                    local.kouhatsuKasanSave = Context.practiceConfig.getKouhatsuKasan();
                    Context.practiceConfig.setKouhatsuKasan("外来後発医薬品使用体制加算２");
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
                .thenAcceptAsync(ignore ->
                        Context.practiceConfig.setKouhatsuKasan(local.kouhatsuKasanSave))
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
