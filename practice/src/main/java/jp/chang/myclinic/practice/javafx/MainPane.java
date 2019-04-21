package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Window;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.*;
import jp.chang.myclinic.practice.javafx.globalsearch.GlobalSearchDialog;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.ListSettingDialog;
import jp.chang.myclinic.practice.javafx.prescexample.EditPrescExampleDialog;
import jp.chang.myclinic.practice.javafx.prescexample.NewPrescExampleDialog;
import jp.chang.myclinic.practice.javafx.refer.ReferDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenDialog;
import jp.chang.myclinic.utilfx.AlertDialog;
import jp.chang.myclinic.utilfx.ConfirmDialog;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MainPane extends BorderPane {

    private static Logger logger = LoggerFactory.getLogger(MainPane.class);
    private MenuItem selectVisitMenu;
    private Supplier<Optional<PatientManip>> findPatientManipFun;
    private RecordsPane recordsPane = new RecordsPane();
    private CurrentPatientInfo currentPatientInfo = new CurrentPatientInfo();
    private StackPane patientManipWrapper = new StackPane();
    private PatientManip patientManip;
    private PracticeHelper helper = PracticeHelper.getInstance();

    public MainPane() {
        setTop(createMenu());
        setCenter(createCenter());
        Context.mainStageService.setTitle(createTitle(null));
        Context.integrationService.setOnNewText(this::onNewText);
        Context.currentPatientService.addOnChangeHandler((patient, visitId) -> {
            currentPatientInfo.setPatient(patient);
            if (patient != null) {
                patientManipWrapper.getChildren().setAll(patientManip);
            } else {
                patientManipWrapper.getChildren().clear();
            }
        });
        Context.integrationService.addVisitPageHandler((page, totalPages, visits) -> {
            recordsPane.getChildren().clear();
            setVisits(visits);
        });
    }

    public void simulateSelectVisitMenuChoice() {
        selectVisitMenu.fire();
    }

    public Optional<Record> findRecord(int visitId) {
        return recordsPane.findRecord(visitId);
    }

    public List<Record> listRecord() {
        return recordsPane.listRecord();
    }

    public void simulateClickCashierButton() {
        for (Node node : patientManipWrapper.getChildren()) {
            if (node instanceof PatientManip) {
                PatientManip manip = (PatientManip) node;
                manip.simulateClickCashierButton();
            }
        }
        throw new RuntimeException("cannot find patient manip");
    }

    private Node createMenu() {
        MenuBar menuBar = new MenuBar();
        {
            Menu fileMenu = new Menu("ファイル");
            MenuItem endItem = new MenuItem("終了");
            endItem.setOnAction(event -> Platform.exit());
            fileMenu.getItems().addAll(endItem);
            menuBar.getMenus().add(fileMenu);
        }
        {
            Menu selectMenu = new Menu("患者選択");
            this.selectVisitMenu = new MenuItem("受付患者選択");
            MenuItem searchMenuItem = new MenuItem("患者検索");
            MenuItem recentVisitsItem = new MenuItem("最近の診察");
            MenuItem todaysVisitsItem = new MenuItem("本日の診察");
            selectVisitMenu.setOnAction(event -> doSelectVisit());
            selectVisitMenu.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
            searchMenuItem.setOnAction(event -> doSearchPatient());
            searchMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
            recentVisitsItem.setOnAction(event -> doRecentVisits());
            todaysVisitsItem.setOnAction(event -> doTodaysVisits());
            selectMenu.getItems().addAll(
                    selectVisitMenu,
                    searchMenuItem,
                    recentVisitsItem,
                    todaysVisitsItem);
            menuBar.getMenus().add(selectMenu);
        }
        {
            Menu menu = new Menu("その他");
            MenuItem newVisitItem = new MenuItem("受付");
            MenuItem newVisitOfCurrentPatientItem = new MenuItem("現在診察中患者の再受付");
            MenuItem referItem = new MenuItem("紹介状作成");
            MenuItem shohousenItem = new MenuItem("処方箋作成");
            MenuItem listPrinterSettingItem = new MenuItem("印刷設定の一覧");
            MenuItem searchTextMenuItem = new MenuItem("全文検索");
            MenuItem newPrescExampleMenuItem = new MenuItem("処方例新規入力");
            MenuItem editPrescExampleMenuItem = new MenuItem("処方例編集");
            newVisitItem.setOnAction(evt -> doNewVisit());
            newVisitOfCurrentPatientItem.setOnAction(evt -> doNewVisitOfCurrentPatient());
            referItem.setOnAction(evt -> doRefer(false));
            shohousenItem.setOnAction(evt -> doShohousen());
            listPrinterSettingItem.setOnAction(evt -> doListPrinterSetting());
            searchTextMenuItem.setOnAction(evt -> doGlobalSearchText());
            newPrescExampleMenuItem.setOnAction(evt -> doNewPrescExample());
            editPrescExampleMenuItem.setOnAction(evt -> doEditPrescExample());
            menu.getItems().addAll(
                    newVisitItem,
                    newVisitOfCurrentPatientItem,
                    referItem,
                    shohousenItem,
                    listPrinterSettingItem,
                    searchTextMenuItem,
                    newPrescExampleMenuItem,
                    editPrescExampleMenuItem
            );
            menuBar.getMenus().add(menu);
        }
        return menuBar;
    }

    private void doEditPrescExample() {
        EditPrescExampleDialog dialog = new EditPrescExampleDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.show();
    }

    private void doNewPrescExample() {
        NewPrescExampleDialog dialog = new NewPrescExampleDialog();
        dialog.initOwner(getScene().getWindow());
        dialog.show();
    }

    private void doTodaysVisits() {
        Context.frontend.listTodaysVisit()
                .thenAccept(list -> Platform.runLater(() -> {
                    TodaysVisitsDialog dialog = new TodaysVisitsDialog(list);
                    dialog.show();
                }))
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private void doNewVisit() {
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.showAndWait();
    }

    private void doNewVisitOfCurrentPatient() {
        PatientDTO patient = Context.currentPatientService.getCurrentPatient();
        if (patient != null) {
            String confirmText = String.format("(%d) %s%s様を再受付しますか？", patient.patientId,
                    patient.lastName, patient.firstName);
            if (ConfirmDialog.confirm(confirmText, this)) {
                Context.frontend.startVisit(patient.patientId, LocalDateTime.now())
                        .exceptionally(HandlerFX.exceptionally(this));
            }
        }
    }

    private void doListPrinterSetting() {
        PrinterEnv printerEnv = Context.printerEnv;
        List<String> names = printerEnv.listNames();
        new ListSettingDialog(names, printerEnv).show();
    }

    private void doGlobalSearchText() {
        GlobalSearchDialog dialog = new GlobalSearchDialog();
        dialog.show();
    }

    private Node createCenter() {
        HBox root = new HBox(4);
        root.setStyle("-fx-padding: 10");
        root.getChildren().addAll(
                createMainColumn(),
                createSideColumn()
        );
        return root;
    }

    private Node createMainColumn() {
        VBox root = new VBox(4);
        root.getStyleClass().add("main-column");
        HBox.setHgrow(root, Priority.ALWAYS);
        root.getChildren().addAll(
                currentPatientInfo,
                createPatientManip(),
                createRecordNav(),
                createRecords(),
                createRecordNav()
        );
        return root;
    }

    private Node createSideColumn() {
        VBox root = new VBox(4);
        root.getStyleClass().add("side-column");
        root.getChildren().addAll(
                new DiseasesPane()
        );
        return root;
    }

    private Node createRecordNav() {
        RecordNav nav = new RecordNav();
        Context.integrationService.addVisitPageHandler((page, totalPages, visits) -> {
            nav.setTotalPages(totalPages);
            nav.setCurrentPage(page);
        });
        return nav;
    }

    private Node createPatientManip() {
        this.patientManip = new PatientManip() {
            @Override
            protected void onCashier() {
                doCashier();
            }

            @Override
            protected void onEndPatient() {
                doEndPatient();
            }

            @Override
            protected void onSearchText() {
                doSearchText();
            }

            @Override
            protected void onRefer() {
                doRefer(true);
            }
        };
        return patientManipWrapper;
    }

    private boolean cancelEndPatient() {
        for (Window w : Window.getWindows()) {
            if (w instanceof ShoukiForm) {
                String msg = "閉じられていない詳記入力フォームがありますが、このまま、この診察を終了しますか？";
                if (ConfirmDialog.confirm(msg, this)) {
                    ((ShoukiForm) w).close();
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    private void doCashier() {
        if (cancelEndPatient()) {
            return;
        }
        int visitId = Context.currentPatientService.getCurrentVisitId();
        if (visitId > 0) {
            Context.frontend.getMeisai(visitId)
                    .thenAccept(meisai -> Platform.runLater(() -> {
                        CashierDialog dialog = new CashierDialog(meisai, visitId);
                        dialog.showAndWait();
                    }))
                    .exceptionally(HandlerFX.exceptionally(this));
        }
    }

    private void doEndPatient() {
        if (cancelEndPatient()) {
            return;
        }
        helper.endPatient();
    }

    private void doSearchText() {
        PatientDTO patient = Context.currentPatientService.getCurrentPatient();
        if (patient != null) {
            SearchTextDialog dialog = new SearchTextDialog(patient.patientId);
            dialog.showAndWait();
        }
    }

    private void doRefer(boolean includePatientInfo) {
        PrinterEnv printerEnv = Context.printerEnv;
        String printerSetting = Context.getReferPrinterSetting();
        ReferDialog dialog = new ReferDialog();
        dialog.setPrinterEnv(printerEnv);
        dialog.setDefaultPrinterSetting(printerSetting);
        PatientDTO patient = Context.currentPatientService.getCurrentPatient();
        if (includePatientInfo && patient != null) {
            dialog.setPatient(patient);
        }
        dialog.setIssueDate(LocalDate.now());
        dialog.show();
    }

    private void doShohousen() {
        try {
            PrinterEnv printerEnv = Context.printerEnv;
            ShohousenDialog dialog = new ShohousenDialog();
            dialog.setPrinterEnv(printerEnv);
            Context.frontend.getClinicInfo()
                    .thenAcceptAsync(clinicInfo -> {
                                dialog.setClinicInfo(clinicInfo);
                                dialog.setDoctorName(clinicInfo.doctorName);
                                dialog.show();
                            },
                            Platform::runLater)
                    .exceptionally(HandlerFX.exceptionally(this));
        } catch (Exception ex) {
            logger.error("Failed to do shohousen.", ex);
            AlertDialog.alert("処方箋ダイアログの表示に失敗しました。", ex, this);
        }
    }

    public void setVisits(List<VisitFull2DTO> visits) {
        recordsPane.getChildren().clear();
        if (visits == null) {
            return;
        }
        List<Integer> shinryouIds = visits.stream().flatMap(v -> v.shinryouList.stream())
                .map(s -> s.shinryou.shinryouId).collect(Collectors.toList());
        List<Integer> drugIds = visits.stream().flatMap(v -> v.drugs.stream())
                .map(d -> d.drug.drugId).collect(Collectors.toList());
        List<Integer> visitIds = visits.stream().map(vf -> vf.visit.visitId).collect(Collectors.toList());
        class Local {
            private Map<Integer, ShinryouAttrDTO> shinryouAttrMap;
            private Map<Integer, DrugAttrDTO> drugAttrMap;
        }
        Local local = new Local();
        Frontend frontend = Context.frontend;
        frontend.batchGetShinryouAttr(shinryouIds)
                .thenCompose(attrList -> {
                    Map<Integer, ShinryouAttrDTO> shinryouAttrMap = new HashMap<>();
                    attrList.forEach(attr -> shinryouAttrMap.put(attr.shinryouId, attr));
                    local.shinryouAttrMap = shinryouAttrMap;
                    return frontend.batchGetDrugAttr(drugIds);
                })
                .thenCompose(attrList -> {
                    Map<Integer, DrugAttrDTO> drugAttrMap = new HashMap<>();
                    attrList.forEach(attr -> drugAttrMap.put(attr.drugId, attr));
                    local.drugAttrMap = drugAttrMap;
                    return frontend.batchGetShouki(visitIds);
                })
                .thenAccept(shoukiList -> {
                    Map<Integer, ShoukiDTO> shoukiMap = new HashMap<>();
                    shoukiList.forEach(s -> shoukiMap.put(s.visitId, s));
                    Platform.runLater(() ->
                            visits.forEach(v -> recordsPane.addRecord(v, local.shinryouAttrMap,
                                    local.drugAttrMap, shoukiMap)));
                })
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private Node createRecords() {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);
        sp.setContent(recordsPane);
        return sp;
    }

    private void doSelectVisit() {
        PracticeFun.listWqueue()
                .thenAcceptAsync(list -> {
                    SelectFromWqueueDialog dialog = new SelectFromWqueueDialog(list);
                    dialog.show();
                }, Platform::runLater)
                .exceptionally(ex -> {
                    logger.error("Failed list wqueue for exam.", ex);
                    Platform.runLater(() ->
                            AlertDialog.alert("受付患者リストの取得に失敗しました。", ex, this));
                    return null;
                });
    }

    private void doSearchPatient() {
        SearchPatientDialog dialog = new SearchPatientDialog();
        dialog.show();
    }

    private void doRecentVisits() {
        Frontend frontend = Context.frontend;
        frontend.listRecentVisitWithPatient(0, 30)
                .thenAccept(result -> {
                    RecentVisitsDialog dialog = new RecentVisitsDialog(result);
                    dialog.setCallback(patient -> {
                        helper.startPatient(patient)
                                .thenAcceptAsync(v -> dialog.close(), Platform::runLater)
                                .exceptionally(HandlerFX.exceptionally(this));
                    });
                    dialog.show();
                })
                .exceptionally(HandlerFX.exceptionally(this));
    }

    private String createTitle(PatientDTO patient) {
        if (patient == null) {
            return "診察";
        } else {
            String title = String.format("診察 (%d) %s%s",
                    patient.patientId,
                    patient.lastName,
                    patient.firstName);
            return title;
        }
    }

    private void onNewText(TextDTO entered) {
        findRecord(entered.visitId)
                .ifPresent(record -> record.appendText(entered));
    }

}
