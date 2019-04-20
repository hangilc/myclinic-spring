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

public class AlertDialog extends Stage {

    public static void alert(String message, Window owner){
        AlertDialog dialog = new AlertDialog(message, owner);
        dialog.showAndWait();
    }

    public static void alert(String message, Node node){
        alert(message, node.getScene().getWindow());
    }

    public static void alert(String message, Exception ex, Node node){
        alert(message + "\n" + ex.toString(), node);
    }

    public static void alert(String message, Exception ex, Window owner){
        alert(message + "\n" + ex.toString(), owner);
    }

    private Button okButton = new Button("OK");

    private AlertDialog(String message, Window owner) {
        Pane mainPane = createPane(message);
        mainPane.setStyle("-fx-padding: 10px");
        mainPane.setPrefWidth(260);
        setScene(new Scene(mainPane));
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL);
        sizeToScene();
    }

    public void simulateClickOkButton(){
        okButton.fire();
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
        okButton.setOnAction(event -> close());
        hbox.getChildren().addAll(okButton);
        return hbox;
    }

}
