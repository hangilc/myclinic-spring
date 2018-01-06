package jp.chang.myclinic.reception.javafx;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.HokenListDTO;
import jp.chang.myclinic.dto.PatientDTO;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class PatientWithHokenStage extends Stage {

    private BooleanProperty currentActiveOnly = new SimpleBooleanProperty(true);
    private HokenListDTO hokenList;

    public PatientWithHokenStage(PatientDTO patient, HokenListDTO hokenList){
        this.hokenList = hokenList;
        VBox root = new VBox(4);
        root.setFillWidth(true);
        {
            VBox vbox = new VBox(4);
            vbox.setMaxWidth(360);
            PatientInfo patientInfo = new PatientInfo();
            patientInfo.setPatient(patient);
            HBox hbox = new HBox(4);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            Button editPatientButton = new Button("編集");
            hbox.getChildren().add(editPatientButton);
            vbox.getChildren().addAll(patientInfo, hbox);
            TitledPane titledPane = new TitledPane("基本情報", vbox);
            titledPane.setCollapsible(false);
            root.getChildren().add(titledPane);
        }
        {
            VBox vbox = new VBox(4);
            vbox.setFillWidth(true);
            HokenTable hokenTable = new HokenTable();
            {
                HBox hbox = new HBox(4);
                hbox.setMaxWidth(Double.MAX_VALUE);
                hokenTable.setHokenList(currentActiveOnly.get() ? filteredHokenList() : hokenList);
                hbox.getChildren().add(hokenTable);
                {
                    VBox buttons = new VBox(4);
                    Button editButton = new Button("編集");
                    Button deleteButton = new Button("削除");
                    buttons.getChildren().addAll(editButton, deleteButton);
                    hbox.getChildren().add(buttons);
                }
                HBox.setHgrow(hokenTable, Priority.ALWAYS);
                vbox.getChildren().add(hbox);
            }
            {
                CheckBox checkBox = new CheckBox("現在有効のみ");
                checkBox.selectedProperty().bindBidirectional(currentActiveOnly);
                currentActiveOnly.addListener((Observable observable) -> {
                    hokenTable.setHokenList(currentActiveOnly.get() ? filteredHokenList() : hokenList);
                });
                vbox.getChildren().add(checkBox);
            }
            {
                HBox row = new HBox(4);
                Button newShahokokuhoButton = new Button("新規社保国保");
                Button newKoukikoureiButton = new Button("新規後期高齢");
                Button newKouhiButton = new Button("新規公費負担");
                newShahokokuhoButton.setOnAction(event -> doNewShahokokuho());
                newKoukikoureiButton.setOnAction(event -> doNewKoukikourei());
                newKouhiButton.setOnAction(event -> doNewKouhi());
                row.getChildren().addAll(newShahokokuhoButton, newKoukikoureiButton, newKouhiButton);
                vbox.getChildren().add(row);
            }
            TitledPane titledPane = new TitledPane("保険情報", vbox);
            titledPane.setCollapsible(false);
            root.getChildren().add(titledPane);
        }
        {
            HBox row = new HBox(4);
            row.setAlignment(Pos.CENTER_RIGHT);
            Button closeButton = new Button("閉じる");
            closeButton.setOnAction(event -> close());
            row.getChildren().add(closeButton);
            root.getChildren().add(row);
        }
        root.setStyle("-fx-padding: 10");
        Scene scene = new Scene(root, 500, 660);
        setScene(scene);
        sizeToScene();
    }

    private HokenListDTO filteredHokenList(){
        HokenListDTO filtered = new HokenListDTO();
        String curr = LocalDate.now().toString();
        if( hokenList.shahokokuhoListDTO != null ) {
            filtered.shahokokuhoListDTO = hokenList.shahokokuhoListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if( hokenList.koukikoureiListDTO != null ) {
            filtered.koukikoureiListDTO = hokenList.koukikoureiListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if( hokenList.roujinListDTO != null ) {
            filtered.roujinListDTO = hokenList.roujinListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        if( hokenList.kouhiListDTO != null ) {
            filtered.kouhiListDTO = hokenList.kouhiListDTO.stream()
                    .filter(h -> isCurrent(h.validFrom, h.validUpto, curr)).collect(Collectors.toList());
        }
        return filtered;
    }

    private boolean isCurrent(String validFrom, String validUpto, String current){
        return validFrom.compareTo(current) <= 0 &&
                (validUpto == null || validUpto.equals("0000-00-00") || validUpto.compareTo(current) >= 0);
    }

    private void doNewShahokokuho(){
        EditShahokokuhoStage stage = new EditShahokokuhoStage();
        stage.showAndWait();
    }

    private void doNewKoukikourei(){
        EditKoukikoureiStage stage = new EditKoukikoureiStage();
        stage.showAndWait();
    }

    private void doNewKouhi(){
        EditKouhiStage stage = new EditKouhiStage();
        stage.showAndWait();
    }

}
