package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.practice.javafx.parts.MeisaiDisp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashierDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(CashierDialog.class);

    public CashierDialog(MeisaiDTO meisai) {
        VBox root = new VBox(4);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getStyleClass().add("cashier-dialog");
        root.getChildren().addAll(
                createDisp(meisai),
                createTotalTen(meisai.totalTen),
                createCharge(meisai),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createDisp(MeisaiDTO meisai) {
        MeisaiDisp disp = new MeisaiDisp(meisai);
        return disp;
    }

    private Node createTotalTen(int totalTen) {
        HBox hbox = new HBox(4);
        hbox.getStyleClass().add("total-ten");
        String text = String.format("総点 %,d 点", totalTen);
        hbox.getChildren().add(new Label(text));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        return hbox;
    }

    private Node createCharge(MeisaiDTO meisai) {
        StackPane wrapper = new StackPane();
        wrapper.getChildren().add(createChargeDisp(wrapper, meisai.charge, meisai));
        return wrapper;
    }

    private Node createChargeDisp(Pane wrapper, int currentCharge, MeisaiDTO meisai) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label(String.format("請求額 %,d 円 (負担割 %d 割)", currentCharge, meisai.futanWari));
        Hyperlink modifyLink = new Hyperlink("変更");
        hbox.getChildren().addAll(
                label,
                modifyLink
        );
        modifyLink.setOnAction(evt -> {
            Node form = createEditForm(
                    () -> {
                    },
                    () -> {
                        wrapper.getChildren().setAll(hbox);
                    }
            );
            wrapper.getChildren().setAll(form);
        });
        return hbox;
    }

    private Node createEditForm(Runnable applyCallback, Runnable cancelCallback) {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        TextField textField = new TextField("");
        textField.getStyleClass().add("charge-input-field");
        Hyperlink enterLink = new Hyperlink("適用");
        Hyperlink cancelLink = new Hyperlink("キャンセル");
        cancelLink.setOnAction(evt -> cancelCallback.run());
        hbox.getChildren().addAll(
                new Label("変更額 "),
                textField,
                new Label("円"),
                enterLink,
                cancelLink
        );
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

}
