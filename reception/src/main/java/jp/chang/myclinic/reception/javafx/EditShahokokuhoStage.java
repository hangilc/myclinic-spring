package jp.chang.myclinic.reception.javafx;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.reception.lib.RadioButtonGroup;
import jp.chang.myclinic.reception.converter.ShahokokuhoConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EditShahokokuhoStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);

    private StringProperty hokenshaBangou = new SimpleStringProperty("");
    private StringProperty hihokenshaKigou = new SimpleStringProperty("");
    private StringProperty hihokenshaBangou = new SimpleStringProperty("");
    private IntegerProperty honnin = new SimpleIntegerProperty();
    private Property<LocalDate> validFrom = new SimpleObjectProperty<LocalDate>();
    private Property<LocalDate> validUpto = new SimpleObjectProperty<LocalDate>();
    private IntegerProperty kourei = new SimpleIntegerProperty();

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
        root.setStyle("-fx-padding: 10");
        Scene scene = new Scene(root);
        setScene(scene);
        sizeToScene();
    }

    private void doEnter(){
        ShahokokuhoDTO data = new ShahokokuhoDTO();
        ShahokokuhoConverter cvt = new ShahokokuhoConverter();
        List<String> errs = new ArrayList<>();
        cvt.convertToHokenshaBangou(hokenshaBangou.get(), errs, val -> { data.hokenshaBangou = val; });
        cvt.convertToHihokenshaKigou(hihokenshaKigou.get(), errs, val -> { data.hihokenshaKigou = val; });
        cvt.convertToHihokenshaBangou(hihokenshaBangou.get(), errs, val -> { data.hihokenshaBangou = val; });
        cvt.convertToHonninKazoku(honnin.getValue(), errs, val -> { data.honnin = val; });
        cvt.convertToValidFrom(validFrom.getValue(), errs, val -> {  data.validFrom = val; });
        cvt.convertToValidUpto(validUpto.getValue(), errs, val -> { data.validUpto = val; });
        cvt.convertToKourei(kourei.getValue(), errs, val -> { data.kourei = val; });
        cvt.integrationCheck(data, errs);
        if( errs.size() > 0 ){
            System.out.println(data);
            System.out.println(errs);
        } else {
            System.out.println(data);
        }
    }

}
