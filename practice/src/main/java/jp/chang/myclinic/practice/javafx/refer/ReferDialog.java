package jp.chang.myclinic.practice.javafx.refer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.javafx.parts.SexInput;
import jp.chang.myclinic.practice.javafx.parts.drawerpreview.DrawerPreviewDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class ReferDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ReferDialog.class);
    private ComboBox<String> titleInput;
    private TextField hospitalInput = new TextField("");
    private TextField sectionInput = new TextField("");
    private TextField doctorInput = new TextField("");
    private TextField patientNameInput = new TextField("");
    private TextField birthdayInput = new TextField("");
    private TextField ageInput = new TextField("");
    private SexInput sexInput = new SexInput();
    private TextField diagnosisInput = new TextField("");
    private TextField issueDateInput = new TextField("");
    private TextArea mainContentInput = new TextArea("");

    public ReferDialog() {
        setTitle("紹介状作成");
        VBox root = new VBox(4);
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getStyleClass().add("refer-dialog");
        root.getChildren().addAll(
                createCommands(),
                createPropertiesInput(),
                createMainContent()
        );
        setScene(new Scene(root));
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button previewButton = new Button("プレビュー");
        previewButton.setOnAction(evt -> doPreview());
        hbox.getChildren().addAll(previewButton);
        return hbox;
    }

    private Node createPropertiesInput(){
        DispGrid part = new DispGrid();
        part.rightAlignFirstColumn();
        ageInput.getStyleClass().add("age-input");
        part.addRow("タイトル", createTitleInput());
        part.addRow("紹介先", hospitalInput);
        part.addRow("紹介科", sectionInput);
        part.addRow("紹介医師", createHBox(doctorInput, new Label("先生")));
        part.addRow("氏名", patientNameInput);
        part.addRow("生年月日", birthdayInput);
        part.addRow("年齢", createHBox(ageInput, new Label("才")));
        part.addRow("性別", sexInput);
        part.addRow("診断名", diagnosisInput);
        part.addRow("作成日", issueDateInput);
        return part;
    }

    private Node createTitleInput(){
        titleInput = new ComboBox<>();
        titleInput.getStyleClass().add("title-input");
        titleInput.getItems().addAll("紹介状", "ご報告");
        titleInput.setEditable(true);
        titleInput.getSelectionModel().select("紹介状");
        return titleInput;
    }

    private HBox createHBox(Node... children){
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.getChildren().addAll(children);
        return hbox;
    }

    private Node createMainContent(){
        return mainContentInput;
    }

    private String composeReferDoctor(){
        String section = sectionInput.getText();
        if( section.isEmpty() ){
            section = "　　　　　　";
        }
        String doctorName = doctorInput.getText();
        if( doctorName.isEmpty() ){
            doctorName = "　　　 　　　";
        }
        return String.format("%s　%s 先生 御机下", section, doctorName);
    }

    private String composePatientName(){
        String name = patientNameInput.getText();
        if( name.isEmpty() ){
            name = "　　　 　　　";
        }
        return String.format("%s 様", name);
    }

    private String composePatientInfo(){
        String birthday = birthdayInput.getText();
        if( birthday.isEmpty() ){
            birthday = "　　   年   月   日";
        }
        String age = ageInput.getText();
        if( age.isEmpty() ){
            age = "    ";
        }
        Sex sex = sexInput.getSelectionModel().getSelectedItem();
        String sexText = sex == null ? "　 " : sex.getKanji();
        return String.format("%s生 %s才 %s性", birthday, age, sexText);
    }

    private void doPreview(){
        ReferDrawer drawer = new ReferDrawer();
        drawer.setTitle(getReferTitle());
        drawer.setReferHospital(hospitalInput.getText());
        drawer.setReferDoctor(composeReferDoctor());
        drawer.setPatientName(composePatientName());
        drawer.setPatientInfo(composePatientInfo());
        drawer.setDiagnosis(diagnosisInput.getText());
        drawer.setIssueDate(issueDateInput.getText());
        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog();
        previewDialog.setTitle("紹介状のプレビュー");
        previewDialog.setScaleFactor(0.7);
        previewDialog.setContentSize(PaperSize.A4.getWidth(), PaperSize.A4.getHeight());
        previewDialog.setOps(drawer.getOps());
        try {
            previewDialog.setPrinterEnv(PracticeEnv.INSTANCE.getMyclinicEnv().getPrinterEnv());
            Properties properties = PracticeEnv.INSTANCE.getAppProperties();
            String settingName = properties.getProperty(PracticeEnv.PRINTER_SETTING_KEY);
            previewDialog.setPrintSettingName(settingName);
        } catch (IOException e) {
            logger.error("Failed to get printer env", e);
            GuiUtil.alertException("入出力エラーが発生しました。", e);
        }
        previewDialog.show();
    }

    private String getReferTitle(){
        return titleInput.getSelectionModel().getSelectedItem();
    }
}
