package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.refer.ReferDialog;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeService;

public class MainPane extends BorderPane {

    public MainPane(){
        setTop(createMenu());
        setCenter(createCenter());
    }

    private Node createMenu(){
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
            selectVisitMenu.setOnAction(event -> doSelectVisit());
            searchMenuItem.setOnAction(event -> doSearchPatient());
            recentVisitsItem.setOnAction(event -> doRecentVisits());
            selectMenu.getItems().addAll(selectVisitMenu, searchMenuItem, recentVisitsItem);
            menuBar.getMenus().add(selectMenu);
        }
        {
            Menu menu = new Menu("その他");
            MenuItem newVisitItem = new MenuItem("受付");
            MenuItem referItem = new MenuItem("紹介状作成");
            newVisitItem.setOnAction(evt -> doNewVisit());
            referItem.setOnAction(evt -> doRefer());
            menu.getItems().addAll(newVisitItem, referItem);
            menuBar.getMenus().add(menu);
        }
        return menuBar;
    }

    private void doNewVisit(){
        NewVisitDialog dialog = new NewVisitDialog();
        dialog.showAndWait();
    }

    private Node createCenter(){
        HBox root = new HBox(4);
        root.setStyle("-fx-padding: 10");
        root.getChildren().addAll(
                createMainColumn(),
                createSideColumn()
        );
        return root;
    }

    private Node createMainColumn(){
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

    private Node createSideColumn(){
        VBox root = new VBox(4);
        root.getStyleClass().add("side-column");
        root.getChildren().addAll(
            new DiseasesPane()
        );
        return root;
    }

    private Node createRecordNav(){
        RecordNav nav = new RecordNav();
        nav.totalPagesProperty().bind(PracticeEnv.INSTANCE.totalRecordPagesProperty());
        nav.currentPageProperty().bind(PracticeEnv.INSTANCE.currentRecordPageProperty());
        return nav;
    }

    private Node createPatientManip(){
        StackPane wrapper = new StackPane();
        PatientManip patientManip = new PatientManip(){
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
            if( newValue != null ){
                wrapper.getChildren().setAll(patientManip);
            } else {
                wrapper.getChildren().clear();
            }
        });
        return wrapper;
    }

    private void doCashier(){
        int visitId = PracticeEnv.INSTANCE.getCurrentVisitId();
        if( visitId > 0 ) {
            Service.api.getMeisai(visitId)
                    .thenAccept(meisai -> Platform.runLater(() ->{
                        CashierDialog dialog = new CashierDialog(meisai, visitId);
                        dialog.showAndWait();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void doEndPatient(){
        PracticeLib.endPatient();
    }

    private void doSearchText(){
        PatientDTO patient = PracticeEnv.INSTANCE.getCurrentPatient();
        if( patient != null ){
            SearchTextDialog dialog = new SearchTextDialog(patient.patientId);
            dialog.showAndWait();
        }
    }

    private void doRefer(){
        ReferDialog dialog = new ReferDialog();
        dialog.show();
    }

    private void doRefer(PatientDTO patient){
        if( patient == null ){
            doRefer();
        } else {
            doRefer();
        }
    }

    private Node createRecords(){
        ScrollPane sp = new ScrollPane();
        sp.setFitToWidth(true);
        VBox.setVgrow(sp, Priority.ALWAYS);
        RecordsPane recordsPane = new RecordsPane();
        PracticeEnv.INSTANCE.pageVisitsProperty().addListener((obs, oldValue, newValue) -> {
            recordsPane.getChildren().clear();
            if( newValue != null ){
                newValue.forEach(recordsPane::addRecord);
            }
        });
        sp.setContent(recordsPane);
        return sp;
    }

    private void doSelectVisit(){
        PracticeLib.listWqueue(list -> {
            SelectFromWqueueDialog dialog = new SelectFromWqueueDialog(list);
            dialog.show();
        });
    }

    private void doSearchPatient(){
        SearchPatientDialog dialog = new SearchPatientDialog();
        dialog.show();
    }

    private void doRecentVisits(){
        PracticeService.listRecentVisits(list -> {
            RecentVisitsDialog dialog = new RecentVisitsDialog(list);
            dialog.setCallback(patient -> {
                PracticeLib.startPatient(patient, () -> {});
            });
            dialog.show();
        });
    }
}
