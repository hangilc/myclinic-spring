package jp.chang.myclinic.utilfx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfirmDialog extends Stage {

    public static boolean confirm(String message, Node node){
        ConfirmDialog dialog = new ConfirmDialog(message, node);
        dialog.showAndWait();
        return dialog.isOk();
    }

    private Button okButton = new Button("はい");
    private Button noButton = new Button("いいえ");
    private boolean ok = false;

    public ConfirmDialog(String message, Node node){
        this(message, node.getScene().getWindow());
    }

    public ConfirmDialog(String message, Window owner) {
        Pane mainPane = createPane(message);
        mainPane.setStyle("-fx-padding: 10px");
        mainPane.setPrefWidth(260);
        setScene(new Scene(mainPane));
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        sizeToScene();
    }

    public boolean isOk(){
        return ok;
    }

    public void simulateClickOkButton(){
        okButton.fire();
    }

    public void simulateClickNoButton(){
        noButton.fire();
    }

    private Pane createPane(String message){
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                createMessagePart(message),
                createCommanPart()
        );
        return vbox;
    }

    private Node createMessagePart(String message){
        StackPane wrapper = new StackPane();
        wrapper.setStyle("-fx-padding: 10px");
        TextFlow textFlow = new TextFlow();
        textFlow.getChildren().add(new Text(message));
        wrapper.getChildren().add(textFlow);
        return wrapper;
    }

    private Node createCommanPart(){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        okButton.setOnAction(event -> { this.ok = true; close(); });
        noButton.setOnAction(event -> close());
        hbox.getChildren().addAll(okButton, noButton);
        return hbox;
    }

}
