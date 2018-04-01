package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.practice.lib.shinryou.RegularShinryou;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AddRegularForm extends VBox {

    private int visitId;
    private List<CheckBox> checks = new ArrayList<>();
    private CheckBox shohouryouCheckBox;
    private CheckBox kouhatsuKasanCheckBox;

    AddRegularForm(int visitId){
        super(4);
        this.visitId = visitId;
        getStyleClass().add("form");
        getChildren().addAll(
                PracticeUtil.createFormTitle("診療行為入力"),
                createInputs(),
                createCommands()
        );
        if( getKouhatsuKasan() != null ) {
            if (shohouryouCheckBox != null && kouhatsuKasanCheckBox != null) {
                shohouryouCheckBox.selectedProperty().addListener((obs, oldValue, newValue) -> {
                    kouhatsuKasanCheckBox.setSelected(newValue);
                });
            }
        }
    }

    private String getKouhatsuKasan(){
        String kasan = PracticeEnv.INSTANCE.getKouhatsuKasan();
        if( kasan != null && kasan.isEmpty() ){
            kasan = null;
        }
        return kasan;
    }

    private Node createInputs(){
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                createTwoColumn(),
                createBottom()
        );
        return vbox;
    }

    private Node createTwoColumn(){
        HBox hbox = new HBox(0);
        VBox left = new VBox(2);
        VBox right = new VBox(2);
        left.prefWidthProperty().bind(hbox.widthProperty().divide(2));
        right.prefWidthProperty().bind(hbox.widthProperty().divide(2));
        left.setOpaqueInsets(new Insets(0, 2, 0, 0));
        right.setOpaqueInsets(new Insets(0, 0, 0, 2));
        setupItems(left, RegularShinryou.getLeftItems());
        setupItems(right, RegularShinryou.getRightItems());
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.getChildren().addAll(left, right);
        return hbox;
    }

    private void setupItems(Pane pane, String[] items){
        String kouhatsu = getKouhatsuKasan();
        for(String item: items) {
            if( item == null ){
                continue;
            }
            if (item.equals("-")) {
                pane.getChildren().add(new Label(" "));
            } else {
                CheckBox check = new CheckBox(item);
                check.setWrapText(true);
                checks.add(check);
                pane.getChildren().add(check);
                if( item.equals("処方料") ){
                    shohouryouCheckBox = check;
                } else if( item.equals(kouhatsu) ){
                    kouhatsuKasanCheckBox = check;
                }
            }
        }
    }

    private Node createBottom(){
        FlowPane pane = new FlowPane(4, 4);
        for(String item: RegularShinryou.getBottomItems()){
            CheckBox check = new CheckBox(item);
            checks.add(check);
            pane.getChildren().add(check);
        }
        return pane;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> onCancel());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    void onEntered(AddRegularForm form){

    }

    private void doEnter(){
        List<String> selected = checks.stream().filter(CheckBox::isSelected).map(CheckBox::getText)
                .collect(Collectors.toList());
        FunJavaFX.batchEnterShinryouByNames(visitId, selected, (shinryouList, conductList) -> {
            Platform.runLater(() -> {
                shinryouList.forEach(this::fireShinryouEnteredEvent);
                conductList.forEach(this::fireConductEnteredEvent);
                onEntered(AddRegularForm.this);
            });
        });
    }

    private void fireShinryouEnteredEvent(ShinryouFullDTO shinryou){
        fireEvent(new ShinryouEnteredEvent(shinryou));
    }

    private void fireConductEnteredEvent(ConductFullDTO conduct){
        fireEvent(new ConductEnteredEvent(conduct));
    }

    void onCancel(){

    }

}
