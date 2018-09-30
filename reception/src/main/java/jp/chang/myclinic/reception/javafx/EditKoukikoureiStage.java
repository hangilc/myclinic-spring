package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.reception.converter.KoukikoureiConverter;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;

import java.time.LocalDate;

public class EditKoukikoureiStage extends EditHokenBaseStage<KoukikoureiDTO> {

    private StringProperty hokenshaBangou = new SimpleStringProperty();
    private StringProperty hihokenshaBangou = new SimpleStringProperty();
    private ObjectProperty<LocalDate> validFrom = new SimpleObjectProperty<LocalDate>();
    private ObjectProperty<LocalDate> validUpto = new SimpleObjectProperty<LocalDate>();
    private IntegerProperty futanWari = new SimpleIntegerProperty();
    private int koukikoureiId;
    private int patientId;

    public EditKoukikoureiStage(KoukikoureiDTO koukikourei){
        this();
        setTitle("後期高齢保険編集");
        this.koukikoureiId = koukikourei.koukikoureiId;
        this.patientId = koukikourei.patientId;
        this.hokenshaBangou.setValue(koukikourei.hokenshaBangou + "");
        this.hihokenshaBangou.setValue(koukikourei.hihokenshaBangou + "");
        this.validFrom.setValue(LocalDate.parse(koukikourei.validFrom));
        this.validUpto.setValue(
                (koukikourei.validUpto == null || "0000-00-00".equals(koukikourei.validUpto) ?
                        LocalDate.MAX: LocalDate.parse(koukikourei.validUpto))
        );
        this.futanWari.setValue(koukikourei.futanWari);
    }

    public EditKoukikoureiStage(){
        setTitle("新規後期高齢保険入力");
        VBox root = new VBox(4);
        {
            Form form = new Form();
            {
                TextField hokenshaBangouInput = new TextField();
                hokenshaBangouInput.setPrefWidth(160);
                hokenshaBangou.bindBidirectional(hokenshaBangouInput.textProperty());
                form.add("保険者番号", hokenshaBangouInput);
            }
            {
                TextField hihokenshaBangouInput = new TextField();
                hihokenshaBangouInput.setPrefWidth(160);
                hihokenshaBangou.bindBidirectional(hihokenshaBangouInput.textProperty());
                form.add("被保険者番号", hihokenshaBangouInput);
            }
            {
                DateInput validFromInput = new DateInput();
                validFromInput.setGengouItems(Gengou.Recent.toArray(new Gengou[]{}));
                validFromInput.selectGengou(Gengou.Current);
                validFrom.bindBidirectional(validFromInput.valueProperty());
                form.add("交付年月日", validFromInput);
            }
            {
                DateInput validUptoInput = new DateInput();
                validUptoInput.setGengouItems(Gengou.Recent.toArray(new Gengou[]{}));
                validUptoInput.selectGengou(Gengou.Current);
                validUpto.bindBidirectional(validUptoInput.valueProperty());
                form.add("有効期限", validUptoInput);
            }
            {
                HBox row = new HBox(4);
                row.setAlignment(Pos.CENTER_LEFT);
                RadioButtonGroup<Number> group = new RadioButtonGroup<>();
                RadioButton futan1Button = group.createRadioButton("1割", 1);
                RadioButton futan2Button = group.createRadioButton("2割", 2);
                RadioButton futan3Button = group.createRadioButton("3割", 3);
                futan1Button.setSelected(true);
                futanWari.bindBidirectional(group.valueProperty());
                row.getChildren().addAll(futan1Button, futan2Button, futan3Button);
                form.add("負担割", row);
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
        root.setStyle("-fx-padding: 10;");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }

    private void doEnter(){
        KoukikoureiDTO data = new KoukikoureiDTO();
        data.koukikoureiId = this.koukikoureiId;
        data.patientId = this.patientId;
        KoukikoureiConverter cvt = new KoukikoureiConverter();
        cvt.convertToHokenshaBangou(hokenshaBangou.get(), value -> { data.hokenshaBangou = value; });
        cvt.convertToHihokenshaBangou(hihokenshaBangou.get(), value -> { data.hihokenshaBangou = value; });
        cvt.convertToValidFrom(validFrom.getValue(), value -> { data.validFrom = value; });
        cvt.convertToValidUpto(validUpto.getValue(), value -> { data.validUpto = value; });
        cvt.convertToFutanWari(futanWari.get(), value -> { data.futanWari = value; });
        cvt.integralCheck(data);
        if( cvt.hasError() ){
            String message = String.join("\n", cvt.getErrors());
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.showAndWait();
        } else {
            getEnterProcessor().accept(data);
        }
    }

}
