package jp.chang.myclinic.practice.javafx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.consts.Sex;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CurrentPatientInfo extends VBox {

    private ObjectProperty<PatientDTO> patient = new SimpleObjectProperty<PatientDTO>();

    public CurrentPatientInfo(){
        super(4);
        getStylesheets().add("css/CurrentPatientInfo.css");
        patient.addListener((obs, oldValue, newValue) -> {
            getChildren().clear();
            if( newValue != null ) {
                Hyperlink detailLink = new Hyperlink("詳細");
                detailLink.getStyleClass().add("detail");
                TextFlow upperRow = new TextFlow(
                        new Text(makeText()), detailLink
                );
                setupNameContextMenu(upperRow);
                StackPane lowerRow = new StackPane();
                detailLink.setOnAction(event -> {
                    if( lowerRow.getChildren().size() == 0 ){
                        TextFlow detailText = new TextFlow(new Text(makeDetail()));
                        lowerRow.getChildren().add(detailText);
                    } else {
                        lowerRow.getChildren().clear();
                    }
                });
                getChildren().addAll(upperRow, lowerRow);
            } else {
                getChildren().add(new Text("(選択患者なし)"));
            }
        });
    }

    private void setupNameContextMenu(Node node){
        node.setOnMouseClicked(event -> {
            MouseButton button = event.getButton();
            if( button == MouseButton.SECONDARY ){
                ContextMenu contextMenu = new ContextMenu();
                {
                    MenuItem copyNameItem = new MenuItem("[id] 名前 (よみ) をコピー");
                    copyNameItem.setOnAction(evt -> {
                        PatientDTO patientDTO = patient.getValue();
                        if( patientDTO != null ) {
                            String text = String.format("[%d] %s %s (%s %s)",
                                    patientDTO.patientId,
                                    patientDTO.lastName,
                                    patientDTO.firstName,
                                    patientDTO.lastNameYomi,
                                    patientDTO.firstNameYomi);
                            boolean ok = copyToClipboard(text);
                            if( !ok ){
                                GuiUtil.alertError("クリップボードへのコピーに失敗しました。");
                            }
                        }
                    });
                    contextMenu.getItems().add(copyNameItem);
                }
                {
                    MenuItem copyNameItem = new MenuItem("名前をコピー");
                    copyNameItem.setOnAction(evt -> {
                        PatientDTO patientDTO = patient.getValue();
                        if( patientDTO != null ) {
                            String text = String.format("%s%s",
                                    patientDTO.lastName,
                                    patientDTO.firstName);
                            boolean ok = copyToClipboard(text);
                            if( !ok ){
                                GuiUtil.alertError("クリップボードへのコピーに失敗しました。");
                            }
                        }
                    });
                    contextMenu.getItems().add(copyNameItem);
                }
                contextMenu.show(node, event.getScreenX(), event.getScreenY());
            }
        });
    }

    private boolean copyToClipboard(String text){
        Map<DataFormat,Object> content = new HashMap<>();
        content.put(DataFormat.PLAIN_TEXT, text);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return clipboard.setContent(content);
    }

    public PatientDTO getPatient() {
        return patient.get();
    }

    public ObjectProperty<PatientDTO> patientProperty() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient.set(patient);
    }

    private String makeText(){
        PatientDTO patient = getPatient();
        String birthday = "";
        if( patient.birthday != null && !patient.birthday.equals("0000-00-00") ){
            try {
                LocalDate d = LocalDate.parse(patient.birthday);
                birthday = DateTimeUtil.toKanji(d, DateTimeUtil.kanjiFormatter1) + "生";
                int age = DateTimeUtil.calcAge(d);
                birthday += String.format(" (%d才)", age);
            } catch(Exception ex){
                birthday = "????";
            }
        }
        String sexText = "";
        Sex sex = Sex.fromCode(patient.sex);
        if( sex != null ){
            sexText = sex.getKanji() + "性";
        }
        return String.format("[%d] %s %s (%s %s) %s %s",
                patient.patientId, patient.lastName, patient.firstName,
                patient.lastNameYomi, patient.firstNameYomi,
                birthday, sexText);
    }

    private String makeDetail(){
        PatientDTO patient = getPatient();
        return "住所：" + patient.address + "\n" + "電話：" + patient.phone;
    }

}
