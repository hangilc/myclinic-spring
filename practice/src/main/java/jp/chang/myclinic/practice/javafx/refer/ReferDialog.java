package jp.chang.myclinic.practice.javafx.refer;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.javafx.parts.SexInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                createPropertiesInput(),
                createMainContent()
        );
        setScene(new Scene(root));
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

    private String getReferTitle(){
        return titleInput.getSelectionModel().getSelectedItem();
    }
}
