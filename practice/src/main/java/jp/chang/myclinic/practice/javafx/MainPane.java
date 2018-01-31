package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.lib.PracticeService;
import jp.chang.myclinic.practice.lib.dateinput.PracticeLib;

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
            MenuItem recentVisitsItem = new MenuItem("最近の診察");
            recentVisitsItem.setOnAction(event -> doRecentVisits());
            selectMenu.getItems().addAll(recentVisitsItem);
            menuBar.getMenus().add(selectMenu);
        }
        return menuBar;
    }

    private Node createCenter(){
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        CurrentPatientInfo currentPatientInfo = new CurrentPatientInfo();
        currentPatientInfo.patientProperty().bind(PracticeEnv.INSTANCE.currentPatientProperty());
        root.getChildren().addAll(
                currentPatientInfo,
                createPatientManip(),
                createRecords()
        );
        return root;
    }

    private Node createPatientManip(){
        StackPane wrapper = new StackPane();
        PatientManip patientManip = new PatientManip();
        PracticeEnv.INSTANCE.currentPatientProperty().addListener((obs, oldValue, newValue) -> {
            if( newValue != null ){
                wrapper.getChildren().setAll(patientManip);
            } else {
                wrapper.getChildren().clear();
            }
        });
        return wrapper;
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
