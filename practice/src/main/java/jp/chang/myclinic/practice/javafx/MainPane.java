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
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.*;
import jp.chang.myclinic.practice.javafx.events.EventTypes;
import jp.chang.myclinic.practice.javafx.events.VisitDeletedEvent;
import jp.chang.myclinic.practice.javafx.globalsearch.GlobalSearchDialog;
import jp.chang.myclinic.practice.javafx.prescexample.EditPrescExampleDialog;
import jp.chang.myclinic.practice.javafx.prescexample.NewPrescExampleDialog;
import jp.chang.myclinic.practice.javafx.refer.ReferDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenDialog;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class MainPane extends BorderPane {

    private static Logger logger = LoggerFactory.getLogger(MainPane.class);
    private MenuItem selectVisitMenu;
    private Supplier<Optional<PatientManip>> findPatientManipFun;
    private Frontend frontend = Context.frontend;
    private CurrentPatientService currentPatientService = Context.currentPatientService;
    private MainStageService mainStageService = Context.mainStageService;
    private RecordsPane recordsPane = new RecordsPane();

    public MainPane() {
        setTop(createMenu());
        setCenter(createCenter());
        addEventHandler(EventTypes.visitDeletedEventType, this::onVisitDeleted);
        mainStageService.setTitle(createTitle(null));
    }

    public void setCurrent(PatientDTO patient, int visitId) {
        currentPatientService.setCurrentPatient(patient, visitId);
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
        findPatientManipFun.get().orElseThrow(() -> new RuntimeException("cannot find patient manip"))
                .simulateClickCashierButton();
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

    private void onVisitDeleted(VisitDeletedEvent event) {
        int visitId = event.getVisitId();
        PracticeEnv env = PracticeEnv.INSTANCE;
        if (env.getCurrentVisitId() == visitId) {
            env.setCurrentVisitId(0);
        }
        if (env.getTempVisitId() == visitId) {
            env.setTempVisitId(0);
        }
    }

    private void doTodaysVisits() {
        Context.frontend.listTodaysVisit()
                .thenAccept(list -> Platform.runLater(() -> {
                    TodaysVisitsDialog dialog = new TodaysVisitsDialog(list);
                    dialog.show();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void doNewVisit() {
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.showAndWait();
    }

    private void doNewVisitOfCurrentPatient() {
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        if (patient != null) {
            String confirmText = String.format("(%d) %s%s様を再受付しますか？", patient.patientId,
                    patient.lastName, patient.firstName);
            if (GuiUtil.confirm(confirmText)) {
                Context.frontend.startVisit(patient.patientId, LocalDateTime.now())
                        .exceptionally(HandlerFX::exceptionally);
            }
        }
    }

    private void doListPrinterSetting() {
        PracticeLib.openPrinterSettingList().ifPresent(Stage::show);
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
        CurrentPatientInfo currentPatientInfo = new CurrentPatientInfo();
        currentPatientInfo.patientProperty().bind(PracticeEnv.INSTANCE.currentPatientProperty());
        HBox.setHgrow(root, Priority.ALWAYS);
        root.getChildren().addAll(
                currentPatientInfo,
                createPatientManip(),
                createRecordNav(),
                createRecords(),
                createRecordNav()
        );
        PracticeEnv.INSTANCE.pageVisitsProperty().addListener((obs, oldValue, newValue) -> {
            recordsPane.getChildren().clear();
            setVisits(newValue);
        });
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
        nav.totalPagesProperty().bind(PracticeEnv.INSTANCE.totalRecordPagesProperty());
        nav.currentPageProperty().bind(PracticeEnv.INSTANCE.currentRecordPageProperty());
        return nav;
    }

    private Node createPatientManip() {
        StackPane wrapper = new StackPane();
        PatientManip patientManip = new PatientManip() {
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
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                wrapper.getChildren().setAll(patientManip);
            } else {
                wrapper.getChildren().clear();
            }
        });
        this.findPatientManipFun = () -> {
            for (Node node : wrapper.getChildren()) {
                if (node instanceof PatientManip) {
                    return Optional.of((PatientManip) node);
                }
            }
            return Optional.empty();
        };
        return wrapper;
    }

    private void doCashier() {
        if (!PracticeEnv.INSTANCE.confirmClosingPatient()) {
            return;
        }
        int visitId = PracticeEnv.INSTANCE.getCurrentVisitId();
        if (visitId > 0) {
            Context.frontend.getMeisai(visitId)
                    .thenAccept(meisai -> Platform.runLater(() -> {
                        CashierDialog dialog = new CashierDialog(meisai, visitId);
                        dialog.showAndWait();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doEndPatient() {
        if (!PracticeEnv.INSTANCE.confirmClosingPatient()) {
            return;
        }
        PracticeLib.endPatient();
    }

    private void doSearchText() {
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        if (patient != null) {
            SearchTextDialog dialog = new SearchTextDialog(patient.patientId);
            dialog.showAndWait();
        }
    }

    private void doRefer(boolean includePatientInfo) {
        try {
            PrinterEnv printerEnv = PracticeEnv.INSTANCE.getMyclinicEnv().getPrinterEnv();
            String printerSetting = PracticeEnv.INSTANCE.getAppProperty(PracticeEnv.REFER_PRINTER_SETTING_KEY);
            ReferDialog dialog = new ReferDialog();
            dialog.setPrinterEnv(printerEnv);
            dialog.setDefaultPrinterSetting(printerSetting);
            PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
            if (includePatientInfo && patient != null) {
                dialog.setPatient(patient);
            }
            dialog.setIssueDate(LocalDate.now());
            dialog.show();
        } catch (IOException ex) {
            logger.error("Failed to open refer dialog.", ex);
            GuiUtil.alertException("紹介状ダイアログの表示に失敗しました。", ex);
        }
    }

    private void doShohousen() {
        try {
            PrinterEnv printerEnv = PracticeEnv.INSTANCE.getMyclinicEnv().getPrinterEnv();
            ShohousenDialog dialog = new ShohousenDialog();
            dialog.setPrinterEnv(printerEnv);
            ClinicInfoDTO clinicInfo = PracticeEnv.INSTANCE.getClinicInfo();
            dialog.setClinicInfo(clinicInfo);
            dialog.setDoctorName(clinicInfo.doctorName);
            dialog.show();
        } catch (Exception ex) {
            logger.error("Failed to do shohousen.", ex);
            GuiUtil.alertException("処方箋ダイアログの表示に失敗しました。", ex);
        }
    }

    public CompletableFuture<Void> setVisits(List<VisitFull2DTO> visits) {
        recordsPane.getChildren().clear();
        if (visits == null) {
            return CompletableFuture.completedFuture(null);
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
        return frontend.batchGetShinryouAttr(shinryouIds)
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
                });
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
                            GuiUtil.alertException("受付患者リストの取得に失敗しました。", ex));
                    return null;
                });
    }

    private void doSearchPatient() {
        SearchPatientDialog dialog = new SearchPatientDialog();
        dialog.show();
    }

    private void doRecentVisits() {
        PracticeService.listRecentVisits(list -> {
            RecentVisitsDialog dialog = new RecentVisitsDialog(list);
            dialog.setCallback(patient -> {
                PracticeLib.startPatient(patient, () -> {
                });
            });
            dialog.show();
        });
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


}
