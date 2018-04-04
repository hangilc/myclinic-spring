package jp.chang.myclinic.medicalcheck;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.medicalcheck.dateinput.DateInput;
import jp.chang.myclinic.medicalcheck.lib.GuiUtil;
import jp.chang.myclinic.medicalcheck.lib.SexRadioInput;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Form extends DispGrid {

    private static Logger logger = LoggerFactory.getLogger(Form.class);
    private TextField nameField = new TextField();
    private DateInput birthdayInput = new DateInput();
    private SexRadioInput sexInput = new SexRadioInput(Sex.Female);
    private TextField addressField = new TextField();
    private TextField heightField = new TextField();
    private TextField weightField = new TextField();
    private TextField physicalExamField = new TextField();
    private TextField visualAcuityBareRightField = new TextField();
    private TextField visualAcuityGlassRightField = new TextField();
    private TextField visualAcuityBareLeftField = new TextField();
    private TextField visualAcuityGlassLeftField = new TextField();
    private TextField hearing1000RightField = new TextField("所見なし");
    private TextField hearing4000RightField = new TextField("所見なし");
    private TextField hearing1000LeftField = new TextField("所見なし");
    private TextField hearing4000LeftField = new TextField("所見なし");

    Form() {
        birthdayInput.setGengou(Gengou.Heisei);
        heightField.getStyleClass().add("height-input");
        weightField.getStyleClass().add("weight-input");
        visualAcuityBareRightField.getStyleClass().add("visual-acuity-input");
        visualAcuityGlassRightField.getStyleClass().add("visual-acuity-input");
        visualAcuityBareLeftField.getStyleClass().add("visual-acuity-input");
        visualAcuityGlassLeftField.getStyleClass().add("visual-acuity-input");
        List.of(hearing1000RightField, hearing4000RightField, hearing1000LeftField, hearing4000LeftField)
                .forEach(f -> f.getStyleClass().add("hearing-ability-input"));
        addRow("氏名", nameField);
        addRow("生年月日", birthdayInput);
        addRow("性別", sexInput);
        addRow("住所", addressField);
        addRow("身長", heightField, new Label("cm"));
        addRow("体重", weightField, new Label("kg"));
        addRow("診察", physicalExamField);
        addRow("視力",
                new Label("右"), visualAcuityBareRightField, new Label("("), visualAcuityGlassRightField, new Label(")"),
                new Label("左"), visualAcuityBareLeftField, new Label("("), visualAcuityGlassLeftField, new Label(")")
        );
        addRow("聴力", hearingInput());
    }

    void applyTo(Data data) {
        data.name = nameField.getText();
        birthdayInput.getValue()
                .ifPresent(date -> {
                    data.birthday = DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
                })
                .ifError(errs -> {
                    GuiUtil.alertError(String.join("\n", errs));
                });
        data.sex = sexInput.getValue().getKanji();
        data.address = addressField.getText();
        data.height = getHeightValue();
        data.weight = getWeightValue();
        data.physicalExam = physicalExamField.getText();
        data.visualAcuity = getVisualAcuity();
        data.hearingAbility1000Right = hearing1000RightField.getText();
        data.hearingAbility4000Right = hearing4000RightField.getText();
        data.hearingAbility1000Left = hearing1000LeftField.getText();
        data.hearingAbility4000Left = hearing4000LeftField.getText();
    }

    private Node hearingInput(){
        VBox vbox = new VBox(4);
        HBox right = new HBox(4, new Label("右"), new Label("1000Hz"), hearing1000RightField,
                new Label("4000Hz"), hearing4000RightField);
        right.setAlignment(Pos.CENTER_LEFT);
        HBox left = new HBox(4, new Label("左"), new Label("1000Hz"), hearing1000LeftField,
                new Label("4000Hz"), hearing4000LeftField);
        left.setAlignment(Pos.CENTER_LEFT);
        vbox.getChildren().addAll(right, left);
        return vbox;
    }

    private String getHeightValue() {
        String height = heightField.getText();
        if (height.isEmpty()) {
            height = "     ";
        }
        return height + " cm";
    }

    private String getWeightValue() {
        String weight = weightField.getText();
        if (weight.isEmpty()) {
            weight = "     ";
        }
        return weight + " kg";
    }

    private String getVisualAcuity(){
        String rightBare = visualAcuityBareRightField.getText();
        String rightGlass = visualAcuityGlassRightField.getText();
        String leftBare = visualAcuityBareLeftField.getText();
        String leftGlass = visualAcuityGlassLeftField.getText();
        if( rightGlass.isEmpty() && leftGlass.isEmpty() ){
            return String.format("右 %s　左 %s",
                    formatVisualAcuity(rightBare),
                    formatVisualAcuity(leftBare));
        } else {
            return String.format("右 %s ( %s )　左 %s ( %s )",
                    formatVisualAcuity(rightBare),
                    formatVisualAcuity(rightGlass),
                    formatVisualAcuity(leftBare),
                    formatVisualAcuity(leftGlass));
        }
    }

    private String formatVisualAcuity(String acuity){
        if( acuity.isEmpty() ){
            return "";
        } else {
            try {
                double value = Double.parseDouble(acuity);
                if( value < 0.1 ){
                    return String.format("%.2f", value);
                } else {
                    return String.format("%.1f", value);
                }
            } catch(NumberFormatException ex){
                GuiUtil.alertError("視力の入力が不適切です。：" + acuity);
                return acuity;
            }
        }
    }

}
