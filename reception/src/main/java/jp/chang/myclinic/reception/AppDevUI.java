package jp.chang.myclinic.reception;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AppDevUI extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox vbox = new VBox();
        Button testButton = new Button("テスト");
        vbox.getChildren().addAll(devDateInput(), testButton);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();
    }

    private static Pane devDateInput() throws IOException {
        return new jp.chang.myclinic.reception.javafx.DateInput();
    }


}
