package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.practice.lib.shinryou.RegularShinryou;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AddRegularForm extends VBox {

    private int visitId;
    private List<CheckBox> checks = new ArrayList<>();

    AddRegularForm(int visitId){
        super(4);
        this.visitId = visitId;
        getStyleClass().add("form");
        getChildren().addAll(
                PracticeUtil.createFormTitle("診療行為入力"),
                createInputs(),
                createCommands()
        );
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
        for(String item: RegularShinryou.getLeftItems()){
            if( item.equals("-") ){
                left.getChildren().add(new Label(" "));
            } else {
                CheckBox check = new CheckBox(item);
                check.setWrapText(true);
                checks.add(check);
                left.getChildren().add(check);
            }
        }
        for(String item: RegularShinryou.getRightItems()){
            if( item.equals("-") ){
                right.getChildren().add(new Label(" "));
            } else {
                CheckBox check = new CheckBox(item);
                check.setWrapText(true);
                checks.add(check);
                right.getChildren().add(check);
            }
        }
        hbox.setAlignment(Pos.TOP_LEFT);
        hbox.getChildren().addAll(left, right);
        return hbox;
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

    private void doEnter(){
        List<String> selected = checks.stream().filter(CheckBox::isSelected).map(CheckBox::getText)
                .collect(Collectors.toList());
        PracticeLib.batchEnterShinryouByNames(selected, visitId, (shinryouList, conductList) -> {
            System.out.println("entered shinryou: " + shinryouList);
            System.out.println("entered conduct: " + conductList);
        });
    }

    void onCancel(){

    }

}
