package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;

import java.time.LocalDate;

class EditRoujinStage extends Stage {

    private ObjectProperty<LocalDate> validFrom = new SimpleObjectProperty<LocalDate>();
    private ObjectProperty<LocalDate> validUpto = new SimpleObjectProperty<LocalDate>();
    private IntegerProperty futanWari = new SimpleIntegerProperty();

    public EditRoujinStage(){
        VBox root = new VBox(4);
        {
            Form form = new Form();
            {
                TextField shichousonBangouInput = new TextField();
                shichousonBangouInput.setPrefWidth(160);
                form.add("市町村番号", shichousonBangouInput);
            }
            {
                TextField jukyuushaBangouInput = new TextField();
                jukyuushaBangouInput.setPrefWidth(160);
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

    }
}
