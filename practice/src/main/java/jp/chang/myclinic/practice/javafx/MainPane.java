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
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.events.EventTypes;
import jp.chang.myclinic.practice.javafx.events.VisitDeletedEvent;
import jp.chang.myclinic.practice.javafx.refer.ReferDialog;
import jp.chang.myclinic.practice.javafx.shohousen.ShohousenDialog;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeService;

import java.time.LocalDate;

public class MainPane extends BorderPane {

    public MainPane() {
        setTop(createMenu());
        setCenter(createCenter());
        addEventHandler(EventTypes.visitDeletedEventType, this::onVisitDeleted);
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
            MenuItem selectVisitMenu = new MenuItem("受付患者選択");
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
            MenuItem referItem = new MenuItem("紹介状作成");
            MenuItem shohousenItem = new MenuItem("処方箋作成");
            MenuItem listPrinterSettingItem = new MenuItem("印刷設定の一覧");
            newVisitItem.setOnAction(evt -> doNewVisit());
            referItem.setOnAction(evt -> doRefer());
            shohousenItem.setOnAction(evt -> doShohousen());
            listPrinterSettingItem.setOnAction(evt -> doListPrinterSetting());
            menu.getItems().addAll(
                    newVisitItem,
                    referItem,
                    shohousenItem,
                    listPrinterSettingItem);
            menuBar.getMenus().add(menu);
        }
        return menuBar;
    }

    private void onVisitDeleted(VisitDeletedEvent event){
        int visitId = event.getVisitId();
        PracticeEnv env = PracticeEnv.INSTANCE;
        if( env.getCurrentVisitId() == visitId ){
            env.setCurrentVisitId(0);
        }
        if( env.getTempVisitId() == visitId ){
            env.setTempVisitId(0);
        }
    }

    private void doTodaysVisits(){
        Service.api.listTodaysVisits()
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

    private void doListPrinterSetting() {
        PracticeLib.openPrinterSettingList().ifPresent(Stage::show);
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
                doRefer();
            }
        };
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                wrapper.getChildren().setAll(patientManip);
            } else {
                wrapper.getChildren().clear();
            }
        });
        return wrapper;
    }

    private void doCashier() {
        int visitId = PracticeEnv.INSTANCE.getCurrentVisitId();
        if (visitId > 0) {
            Service.api.getMeisai(visitId)
                    .thenAccept(meisai -> Platform.runLater(() -> {
                        CashierDialog dialog = new CashierDialog(meisai, visitId);
                        dialog.showAndWait();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doEndPatient() {
        PracticeLib.endPatient();
    }

    private void doSearchText() {
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        if (patient != null) {
            SearchTextDialog dialog = new SearchTextDialog(patient.patientId);
            dialog.showAndWait();
        }
    }

    private void doRefer() {
        ReferDialog dialog = new ReferDialog();
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        if (patient != null) {
            dialog.setPatient(patient);
        }
        dialog.setIssueDate(LocalDate.now());
        dialog.show();
    }

    private void doShohousen() {
        ShohousenDialog dialog = new ShohousenDialog();
        ClinicInfoDTO clinicInfo = PracticeEnv.INSTANCE.getClinicInfo();
        dialog.setClinicInfo(clinicInfo);
        dialog.setDoctorName(clinicInfo.doctorName);
        dialog.show();
    }

    private Node createRecords() {
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);
        RecordsPane recordsPane = new RecordsPane();
        PracticeEnv.INSTANCE.pageVisitsProperty().addListener((obs, oldValue, newValue) -> {
            recordsPane.getChildren().clear();
            if (newValue != null) {
                newValue.forEach(recordsPane::addRecord);
            }
        });
        sp.setContent(recordsPane);
        return sp;
    }

    private void doSelectVisit() {
        PracticeLib.listWqueue(list -> {
            SelectFromWqueueDialog dialog = new SelectFromWqueueDialog(list);
            dialog.show();
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
}
