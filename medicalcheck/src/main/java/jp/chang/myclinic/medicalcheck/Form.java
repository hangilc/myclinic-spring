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
import jp.chang.myclinic.medicalcheck.dateinput.Result;
import jp.chang.myclinic.medicalcheck.lib.GuiUtil;
import jp.chang.myclinic.medicalcheck.lib.SexRadioInput;
import jp.chang.myclinic.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

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
    private TextField hearing1000RightField = new TextField("");
    private TextField hearing4000RightField = new TextField("");
    private TextField hearing1000LeftField = new TextField("");
    private TextField hearing4000LeftField = new TextField("");
    private TextField bloodPressureField = new TextField("");
    private TextField ekgField = new TextField("");
    private TextField historyField = new TextField("特記事項なし");
    private TextField chestXpResultField = new TextField("");
    private DateInput chestXpDateInput = new DateInput();

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
        bloodPressureField.getStyleClass().add("blood-pressure-input");
        chestXpDateInput.setGengou(Gengou.MostRecent);
        addRow("氏名", nameField);
        addRow("生年月日", birthdayInput);
        addRow("性別", sexInput);
        addRow("住所", addressField);
        addRow("身長", heightField, new Label("cm"));
        addRow("体重", weightField, new Label("kg"));
        addRow("診察", physicalExamField);
        addRow("視力",
                new Label("右"), visualAcuityBareRightField,
                new Label("("), visualAcuityGlassRightField, new Label(")"),
                new Label("左"), visualAcuityBareLeftField,
                new Label("("), visualAcuityGlassLeftField, new Label(")"));
        addRow("聴力", hearingInput());
        addRow("血圧", bloodPressureField, new Label("mm/Hg"));
        addRow("心電図", ekgField);
        addRow("既往歴", historyField);
        addRow("Ｘ線結果", chestXpResultField);
        addRow("Ｘ線撮影日", chestXpDateInput);
    }

    void applyTo(Data data) {
        data.name = nameField.getText();
        data.birthday = getBirthdayValue();
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
        data.bloodPressure = getBloodPressureValue();
        data.ekg = ekgField.getText();
        data.history = historyField.getText();
        data.chestXpResult = chestXpResultField.getText();
        data.chestXpDate = getChestXpDate();
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

    void setHearingAbilityNormal(){
        String normal = "所見なし";
        hearing1000RightField.setText(normal);
        hearing4000RightField.setText(normal);
        hearing1000LeftField.setText(normal);
        hearing4000LeftField.setText(normal);
    }

    private String getBirthdayValue(){
        return getDate(birthdayInput, date -> {
            return DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
        }, "生年月日の入力が不適切です。");
    }

    private String getHeightValue() {
        String height = heightField.getText();
        if (height.isEmpty()) {
            height = "       ";
        }
        return height + " cm";
    }

    private String getWeightValue() {
        String weight = weightField.getText();
        if (weight.isEmpty()) {
            weight = "       ";
        }
        return weight + " kg";
    }

    private String getVisualAcuity(){
        String rightBare = visualAcuityBareRightField.getText();
        String rightGlass = visualAcuityGlassRightField.getText();
        String leftBare = visualAcuityBareLeftField.getText();
        String leftGlass = visualAcuityGlassLeftField.getText();
        if( rightBare.isEmpty() && rightGlass.isEmpty() && leftBare.isEmpty() && leftGlass.isEmpty() ){
            return "";
        }
        if( rightBare.isEmpty() ){
            rightBare = "     ";
        }
        String right = "右 " + formatVisualAcuity(rightBare);
        if( !rightGlass.isEmpty() ){
            right += String.format(" (%s)", formatVisualAcuity(rightGlass));
        }
        String left = "左 " + formatVisualAcuity(leftBare);
        if( !leftGlass.isEmpty() ){
            left += String.format(" (%s)", formatVisualAcuity(leftGlass));
        }
        return left + "  " + right;
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

    private String getBloodPressureValue(){
        String value = bloodPressureField.getText();
        if( value.isEmpty() ){
            return "";
        } else {
            return value + " mmHg";
        }
    }

    private String getChestXpDate(){
        return getDate(chestXpDateInput, date -> {
            String dateRep = DateTimeUtil.toKanji(date, DateTimeUtil.kanjiFormatter1);
            return String.format("撮影日 %s （直接）", dateRep);
        }, "Ｘ線撮影日の入力が不適切です。");
    }

    private String getDate(DateInput input, Function<LocalDate, String> formatter, String errorMessage){
        if( input.isEmpty() ){
            return "";
        }
        Result<LocalDate, List<String>> result =  input.getValue();
        if( result.hasValue() ){
            LocalDate date = result.getValue();
            return formatter.apply(date);
        } else {
            GuiUtil.alertError(errorMessage + "\n" + String.join("\n", result.getError()));
            return "";
        }
    }

}
