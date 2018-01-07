package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.reception.converter.KouhiConverter;

import java.time.LocalDate;

public class EditKouhiStage extends EditHokenBaseStage<KouhiDTO> {

    private StringProperty futansha = new SimpleStringProperty();
    private StringProperty jukyuusha = new SimpleStringProperty();
    private ObjectProperty<LocalDate> validFrom = new SimpleObjectProperty<LocalDate>();
    private ObjectProperty<LocalDate> validUpto = new SimpleObjectProperty<LocalDate>();

    public EditKouhiStage(){
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
            {
                DateInput validFromInput = new DateInput();
                validFromInput.setGengouItems(Gengou.Recent.toArray(new Gengou[]{}));
                validFromInput.selectGengou(Gengou.Current);
                validFrom.bindBidirectional(validFromInput.valueProperty());
                form.add("資格取得日", validFromInput);
            }
            {
                DateInput validUptoInput = new DateInput();
                validUptoInput.setGengouItems(Gengou.Recent.toArray(new Gengou[]{}));
                validUptoInput.selectGengou(Gengou.Current);
                validUpto.bindBidirectional(validUptoInput.valueProperty());
                form.add("有効期限", validUptoInput);
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
        KouhiDTO data = new KouhiDTO();
        KouhiConverter cvt = new KouhiConverter();
        cvt.convertToFutansha(futansha.get(), value -> { data.futansha = value; });
        cvt.convertToJukyuusha(jukyuusha.get(), value -> { data.jukyuusha = value; });
        cvt.convertToValidFrom(validFrom.getValue(), value -> { data.validFrom = value; });
        cvt.convertToValidUpto(validUpto.getValue(), value -> { data.validUpto = value; });
        if( cvt.hasError() ){
            System.out.println(cvt.getErrors());
            System.out.println(data);
        } else {
            getEnterProcessor().accept(data);
        }
    }

}
