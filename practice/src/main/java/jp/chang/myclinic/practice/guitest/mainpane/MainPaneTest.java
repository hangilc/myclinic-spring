package jp.chang.myclinic.practice.guitest.mainpane;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.CurrentPatientService;
import jp.chang.myclinic.practice.MainStageServiceAdapter;
import jp.chang.myclinic.practice.javafx.MainPane;

import java.util.concurrent.CompletableFuture;

public class MainPaneTest {

    private Stage stage;
    private Pane main;

    public MainPaneTest(Stage stage, Pane mainPane) {
        this.stage = stage;
        this.main = mainPane;
    }

    public void disp(){
        System.out.println("Enter disp");
        createMainPane();
    }

    private CompletableFuture<MainPane> createMainPane(){
        Context.mainStageService = new MainStageServiceAdapter(){
            @Override
            public void setTitle(String title) {
            }
        };
        Context.currentPatientService = new CurrentPatientService();
        return CompletableFuture.supplyAsync(() -> {
            MainPane mainPane = new MainPane();
            main.getChildren().setAll(mainPane);
            stage.sizeToScene();
            System.out.println("MAIN");
            return mainPane;
        }, Platform::runLater);
    }


}
