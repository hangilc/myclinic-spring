package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.util.verify.ErrorMessages;
import jp.chang.myclinic.util.verify.KouhiVerifier;

import java.time.LocalDate;

abstract public class EditKouhiStage extends EditHokenBaseStage {

    private StringProperty futansha = new SimpleStringProperty();
    private StringProperty jukyuusha = new SimpleStringProperty();
    private int kouhiId;
    private int patientId;

    public EditKouhiStage(KouhiDTO kouhi) {
        this();
        this.kouhiId = kouhi.kouhiId;
        this.patientId = kouhi.patientId;
        futansha.setValue(kouhi.futansha + "");
        jukyuusha.setValue(kouhi.jukyuusha + "");
        validFromInput.setValue(LocalDate.parse(kouhi.validFrom));
        validUptoInput.setValue(
                (kouhi.validUpto == null || "0000-00-00".equals(kouhi.validUpto) ?
                        LocalDate.MAX : LocalDate.parse(kouhi.validUpto))
        );
    }

    public EditKouhiStage() {
        VBox root = new VBox(4);
        {
            Form form = new Form();
            {
                TextField futanshaBangouInput = new TextField();
                futanshaBangouInput.setPrefWidth(160);
                futansha.bindBidirectional(futanshaBangouInput.textProperty());
                form.add("負担者番号", futanshaBangouInput);
            }
            {
                TextField jukyuushaBangouInput = new TextField();
                jukyuushaBangouInput.setPrefWidth(160);
                jukyuusha.bindBidirectional(jukyuushaBangouInput.textProperty());
                form.add("受給者番号", jukyuushaBangouInput);
            }
            form.add("交付年月日", validFromInput);
            form.add("有効期限", validUptoInput);
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
        root.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }

    private void doEnter() {
        KouhiDTO data = new KouhiDTO();
        data.kouhiId = this.kouhiId;
        data.patientId = this.patientId;
        ErrorMessages em = new ErrorMessages();
        if (Globals.isCheckingHokenshaBangou()) {
            em.addIfError(
                    KouhiVerifier.verifyFutanshaBangouInput(futansha.get(), value -> data.futansha = value),
                    KouhiVerifier.verifyJukyuushaBangouInput(jukyuusha.get(), value -> data.jukyuusha = value)
            );
        }
        verifyValidFrom(em, KouhiVerifier::verifyValidFrom, value -> data.validFrom = value);
        verifyValidUpto(em, KouhiVerifier::verifyValidUpto, value -> data.validUpto = value);
        if (em.hasError()) {
            String message = em.getErrorMessage();
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        } else {
            onEnter(data);
        }
    }

    abstract void onEnter(KouhiDTO data);

}
