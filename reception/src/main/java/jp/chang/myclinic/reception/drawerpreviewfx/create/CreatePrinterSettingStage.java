package jp.chang.myclinic.reception.drawerpreviewfx.create;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.reception.javafx.Form;

public class CreatePrinterSettingStage extends Stage {

    private TextField nameInput;
    private PrinterInputPane printerInputPane;
    private TextField dxInput;
    private TextField dyInput;
    private TextField scaleInput;

    public CreatePrinterSettingStage() {
        setTitle("新規印刷設定の入力");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        {
            Form form = new Form();
            nameInput = new TextField();
            nameInput.setMaxWidth(160);
            form.add("名前", nameInput);
            printerInputPane = new PrinterInputPane();
            form.add("プリンター", printerInputPane);
            dxInput = new TextField("0");
            dxInput.setMaxWidth(40);
            form.add("dx", dxInput);
            dyInput = new TextField("0");
            dyInput.setMaxWidth(40);
            form.add("dy", dyInput);
            scaleInput = new TextField("1.0");
            scaleInput.setMaxWidth(40);
            form.add("scale", scaleInput);
            root.getChildren().add(form);
        }
        {
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button enterButton = new Button("入力");
            Button cancelButton = new Button("キャンセル");
            hbox.getChildren().addAll(enterButton, cancelButton);
            root.getChildren().add(hbox);
        }
        setScene(new Scene(root));
    }

}
