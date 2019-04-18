package jp.chang.myclinic.practice.guitest.records;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.guitest.GuiTestBase;
import jp.chang.myclinic.practice.javafx.RecordsPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class RecordsPaneTitleTest extends GuiTestBase {

    public RecordsPaneTitleTest(Stage stage, StackPane main) {
        super(stage, main);
    }

    private RecordsPane createPane(){
        RecordsPane pane = new RecordsPane();
        gui(() -> {
            pane.setPrefWidth(680);
            pane.setPrefHeight(Region.USE_COMPUTED_SIZE);
            pane.setMaxHeight(Region.USE_PREF_SIZE);
            ScrollPane scroll = new ScrollPane(pane);
            scroll.setPrefWidth(700);
            scroll.setPrefHeight(600);
            main.getChildren().setAll(scroll);
            stage.sizeToScene();
        });
        return pane;
    }

    private LocalDateTime prevDays(int days){
        return LocalDateTime.now().minus(days, ChronoUnit.DAYS);
    }

    
}
