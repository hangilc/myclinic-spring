package jp.chang.myclinic.reception.javafx;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.reception.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPatientStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(SearchPatientStage.class);

    private StringProperty searchText = new SimpleStringProperty();

    public SearchPatientStage(){
        VBox root = new VBox(4);
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_LEFT);
            {
                TextField searchTextInput = new TextField();
                searchTextInput.textProperty().bindBidirectional(searchText);
                Button searchButton = new Button("検索");
                Button recentlyRegisteredButton = new Button("最近の登録");
                searchButton.setOnAction(event -> doSearch());
                hbox.getChildren().addAll(searchTextInput, searchButton, recentlyRegisteredButton);
            }
            root.getChildren().add(hbox);
        }
        {
            TableView tableView = new TableView();
            tableView.setPrefWidth(425);
            tableView.setPrefHeight(250);
            root.getChildren().add(tableView);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.TOP_LEFT);
            TextArea infoLabel = new TextArea("");
            infoLabel.setPrefWidth(314);
            infoLabel.setPrefHeight(182);
            VBox commandBox = new VBox(4);
            commandBox.setAlignment(Pos.TOP_CENTER);
            commandBox.setFillWidth(true);
            Button editButton = new Button("編集");
            editButton.setMaxWidth(300);
            Button registerButton = new Button("診療受付");
            registerButton.setMaxWidth(300);
            commandBox.getChildren().addAll(editButton, registerButton);
            hbox.getChildren().addAll(infoLabel, commandBox);
            HBox.setHgrow(infoLabel, Priority.ALWAYS);
            root.getChildren().add(hbox);
        }
        root.setStyle("-fx-padding: 10");
        setScene(new Scene(root));
        sizeToScene();
    }

    private void doSearch() {
        String text = searchText.getValue();
        if( text == null || text.isEmpty() ){
            return;
        }
        Service.api.searchPatient(text)
                .thenAccept(list -> {
                    System.out.println(list);
                })
                .exceptionally(ex -> {
                    logger.error("Search patient failed.", ex);
                    Platform.runLater(() -> GuiUtil.alertException(ex));
                    return null;
                });
    }
}
