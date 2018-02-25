package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.parts.MeisaiDisp;
import jp.chang.myclinic.practice.javafx.parts.inplaceediting.InPlaceEditable;
import jp.chang.myclinic.practice.lib.PracticeLib;

import java.util.function.Consumer;

class CashierDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(CashierDialog.class);
    private int visitId;
    private int chargeValue;
    private MeisaiDTO meisai;

    CashierDialog(MeisaiDTO meisai, int visitId) {
        this.meisai = meisai;
        this.visitId = visitId;
        this.chargeValue = meisai.charge;
        VBox root = new VBox(4);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getStyleClass().add("cashier-dialog");
        root.getChildren().addAll(
                new MeisaiDisp(meisai),
                createTotalTen(meisai.totalTen),
                createCharge(),
                createCommands()
        );
        setScene(new Scene(root));
    }

    private Node createTotalTen(int totalTen) {
        HBox hbox = new HBox(4);
        hbox.getStyleClass().add("total-ten");
        String text = String.format("総点 %,d 点", totalTen);
        hbox.getChildren().add(new Label(text));
        hbox.setAlignment(Pos.BASELINE_RIGHT);
        return hbox;
    }

    private Node createCharge() {
        ChargeDisp disp = new ChargeDisp();
        return new InPlaceEditable<>(disp);
    }

    private class ChargeDisp extends HBox implements InPlaceEditable.Editable<Integer> {
        private Consumer<InPlaceEditable.Editor<Integer>> onEditCallback;

        ChargeDisp(){
            setAlignment(Pos.CENTER_LEFT);
            Label label = new Label(String.format("請求額 %,d 円 (負担割 %d 割)", chargeValue, meisai.futanWari));
            Hyperlink modifyLink = new Hyperlink("変更");
            modifyLink.setOnAction(evt -> {
                ChargeEditor editor = new ChargeEditor();
                onEditCallback.accept(editor);
            });
            getChildren().addAll(
                    label,
                    modifyLink
            );
        }

        @Override
        public void setOnEditCallback(Consumer<InPlaceEditable.Editor<Integer>> onEditCallback) {
            this.onEditCallback = onEditCallback;
        }

        @Override
        public Node asNode() {
            return this;
        }
    }

    private class ChargeEditor extends HBox implements InPlaceEditable.Editor<Integer> {
        private Consumer<InPlaceEditable.Editable<Integer>> onEnterCallback;
        private Runnable cancelCallback;

        ChargeEditor(){
            setAlignment(Pos.CENTER_LEFT);
            TextField textField = new TextField("");
            textField.getStyleClass().add("charge-input-field");
            Hyperlink enterLink = new Hyperlink("適用");
            Hyperlink cancelLink = new Hyperlink("キャンセル");
            enterLink.setOnAction(evt -> {
                try {
                    chargeValue = Integer.parseInt(textField.getText());
                    onEnterCallback.accept(new ChargeDisp());
                } catch(NumberFormatException ex){
                    GuiUtil.alertError("請求額の入慮億が不適切です。");
                }
            });
            cancelLink.setOnAction(evt -> cancelCallback.run());
            getChildren().addAll(
                    new Label("変更額 "),
                    textField,
                    new Label("円"),
                    enterLink,
                    cancelLink
            );
        }

        @Override
        public void setOnEnterCallback(Consumer<InPlaceEditable.Editable<Integer>> cb) {
            this.onEnterCallback = cb;
        }

        @Override
        public void setOnCancelCallback(Runnable cb) {
            this.cancelCallback = cb;
        }

        @Override
        public Node asNode() {
            return this;
        }
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter());
        cancelButton.setOnAction(evt -> CashierDialog.this.close());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        PracticeLib.endExam(visitId, chargeValue, () -> close());
    }

}
