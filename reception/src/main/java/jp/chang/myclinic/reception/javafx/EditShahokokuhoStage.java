package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;
import jp.chang.myclinic.util.verify.ErrorMessages;
import jp.chang.myclinic.util.verify.ShahokokuhoVerifier;
import jp.chang.myclinic.utilfx.dateinput.DateInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static jp.chang.myclinic.util.verify.ShahokokuhoVerifier.*;

abstract public class EditShahokokuhoStage extends EditHokenBaseStage {

    private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);

    private StringProperty hokenshaBangou = new SimpleStringProperty("");
    private StringProperty hihokenshaKigou = new SimpleStringProperty("");
    private StringProperty hihokenshaBangou = new SimpleStringProperty("");
    private IntegerProperty honnin = new SimpleIntegerProperty();
    private IntegerProperty kourei = new SimpleIntegerProperty();
    private int shahokokuhoId;
    private int patientId;

    public EditShahokokuhoStage(ShahokokuhoDTO shahokokuho) {
        this();
        setTitle("社保国保編集");
        this.shahokokuhoId = shahokokuho.shahokokuhoId;
        this.patientId = shahokokuho.patientId;
        this.hokenshaBangou.setValue("" + shahokokuho.hokenshaBangou);
        this.hihokenshaKigou.setValue("" + shahokokuho.hihokenshaKigou);
        this.hihokenshaBangou.setValue("" + shahokokuho.hihokenshaBangou);
        this.honnin.setValue(shahokokuho.honnin);
        validFromInput.setValue(LocalDate.parse(shahokokuho.validFrom));
        setValidUpto(shahokokuho.validUpto);
        this.kourei.setValue(shahokokuho.kourei);
    }

    public EditShahokokuhoStage() {
        setTitle("新規社保国保入力");
        VBox root = new VBox(4);
        {
            Form form = new Form();
            {
                TextField hokenshaBangouInput = new TextField();
                hokenshaBangouInput.textProperty().bindBidirectional(hokenshaBangou);
                hokenshaBangouInput.setPrefWidth(140);
                hokenshaBangouInput.setMaxWidth(Control.USE_PREF_SIZE);
                form.add("保険者番号", hokenshaBangouInput);
            }
            {
                HBox row = new HBox(4);
                row.setAlignment(Pos.CENTER_LEFT);
                TextField hihokenshaKigouInput = new TextField();
                TextField hihokenshaBangouInput = new TextField();
                hihokenshaKigouInput.textProperty().bindBidirectional(hihokenshaKigou);
                hihokenshaBangouInput.textProperty().bindBidirectional(hihokenshaBangou);
                row.getChildren().addAll(
                        new Label("記号"),
                        hihokenshaKigouInput,
                        new Label("番号"),
                        hihokenshaBangouInput
                );
                form.add("被保険者", row);
            }
            {
                HBox row = new HBox(4);
                row.setAlignment(Pos.CENTER_LEFT);
                RadioButtonGroup<Number> group = new RadioButtonGroup<>();
                RadioButton honninButton = group.createRadioButton("本人", 1);
                RadioButton kazokuButton = group.createRadioButton("家族", 0);
                kazokuButton.setSelected(true);
                honnin.bindBidirectional(group.valueProperty());
                row.getChildren().addAll(honninButton, kazokuButton);
                form.add("本人・家族", row);
            }
            {
                this.validFromInput = new DateInput(Gengou.Recent);
                validFromInput.setGengou(Gengou.Current);
                form.add("交付年月日", validFromInput);
            }
            {
                this.validUptoInput = new DateInput(Gengou.Recent);
                validUptoInput.setGengou(Gengou.Current);
                validUptoInput.setAllowNull(true);
                form.add("有効期限", validUptoInput);
            }
            {
                HBox row = new HBox(4);
                row.setAlignment(Pos.CENTER_LEFT);
                RadioButtonGroup<Number> group = new RadioButtonGroup<>();
                RadioButton noKoureiButton = group.createRadioButton("高齢でない", 0);
                RadioButton futan1Button = group.createRadioButton("1割", 1);
                RadioButton futan2Button = group.createRadioButton("2割", 2);
                RadioButton futan3Button = group.createRadioButton("3割", 3);
                noKoureiButton.setSelected(true);
                kourei.bindBidirectional(group.valueProperty());
                row.getChildren().addAll(noKoureiButton, futan1Button, futan2Button, futan3Button);
                form.add("高齢", row);
            }
            root.getChildren().add(form);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_RIGHT);
            Button enterButton = new Button("入力");
            Button cancelButton = new Button("キャンセル");
            enterButton.setOnAction(event -> doEnter());
            cancelButton.setOnAction(event -> close());
            row.getChildren().addAll(enterButton, cancelButton);
            root.getChildren().add(row);
        }
        root.getStylesheets().add("css/Main.css");
        root.setStyle("-fx-padding: 10");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }

    private void doEnter() {
        ShahokokuhoDTO data = new ShahokokuhoDTO();
        data.shahokokuhoId = this.shahokokuhoId;
        data.patientId = this.patientId;
        ErrorMessages errs = new ErrorMessages();
        if (Globals.isCheckingHokenshaBangou()) {
            errs.addIfError(
                    verifyHokenshaBangouInput(hokenshaBangou.getValue(), val -> data.hokenshaBangou = val)
            );
        }
        setHihokenshaKigou(hihokenshaKigou.get(), val -> data.hihokenshaKigou = val);
        setHihokenshaBangou(hihokenshaBangou.get(), val -> data.hihokenshaBangou = val);
        errs.addIfError(verifyHonninKazoku(honnin.getValue(), value -> data.honnin = value));
        verifyValidFrom(errs, ShahokokuhoVerifier::verifyValidFrom, value -> data.validFrom = value);
        verifyValidUpto(errs, ShahokokuhoVerifier::verifyValidUpto, value -> data.validUpto = value);
        errs.addIfError(verifyKourei(kourei.getValue(), val -> data.kourei = val));
        if (errs.hasNoError()) {
            errs.addIfError(
                    verifyHihokenshaKigouAndBangou(data.hihokenshaKigou, data.hihokenshaBangou),
                    verifyValidFromAndValidUpto(data.validFrom, data.validUpto)
            );
        }
        if (errs.hasError()) {
            String message = errs.getErrorMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        } else {
            onEnter(data);
        }
    }

    abstract void onEnter(ShahokokuhoDTO data);

}
