package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class EditShahokokuhoStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);

    private StringProperty hokenshaBangou = new SimpleStringProperty();
    private StringProperty hihokenshaKigou = new SimpleStringProperty();
    private StringProperty hihokenshaBangou = new SimpleStringProperty();
    private IntegerProperty honnin = new SimpleIntegerProperty();
    private Property<LocalDate> validFrom = new SimpleObjectProperty<LocalDate>();

    public EditShahokokuhoStage(){
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
                DateInput validFromInput = new DateInput();
                validFrom.bindBidirectional(validFromInput.valueProperty());
                form.add("資格取得日", validFromInput);
            }
            {
                DateInput validUptoInput = new DateInput();
                form.add("有効期限", validUptoInput);
            }
            {
                HBox row = new HBox(4);
                row.setAlignment(Pos.CENTER_LEFT);
                RadioButton noKoureiButton = new RadioButton("高齢でない");
                RadioButton futan1Button = new RadioButton("1割");
                RadioButton futan2Button = new RadioButton("2割");
                RadioButton futan3Button = new RadioButton("3割");
                ToggleGroup group = new ToggleGroup();
                noKoureiButton.setSelected(true);
                group.getToggles().addAll(noKoureiButton, futan1Button, futan2Button, futan3Button);
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
        root.setStyle("-fx-padding: 10");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }

    private void doEnter(){
        System.out.println(hokenshaBangou.get());
        System.out.println(hihokenshaKigou.get());
        System.out.println(hihokenshaBangou.get());
        System.out.println(honnin.get());
        System.out.println(validFrom.getValue());
    }

}
