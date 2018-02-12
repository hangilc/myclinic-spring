package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.practice.javafx.parts.TwoColumn;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;
import jp.chang.myclinic.practice.lib.shinryou.KensaEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class AddKensaForm extends WorkForm {

    private List<CheckBox> checks = new ArrayList<>();

    AddKensaForm(){
        super("検査の入力");
        getChildren().addAll(
                createInputs(),
                createSelectors(),
                createCommands()
        );
    }

    private Node createInputs(){
        TwoColumn twoColumn = new TwoColumn(10);
        stuffInputs(twoColumn.getLeftBox(), KensaEntry.getLeftEntries());
        stuffInputs(twoColumn.getRightBox(), KensaEntry.getRightEntries());
        return twoColumn;
    }

    private Node createSelectors(){
        HBox hbox = new HBox(4);
        Hyperlink selectSetLink = new Hyperlink("セット検査");
        Hyperlink clearLink = new Hyperlink("クリア");
        selectSetLink.setOnAction(event -> {
            checks.forEach(chk -> {
                KensaEntry entry = (KensaEntry)chk.getUserData();
                if( entry.isPreset() ){
                    chk.setSelected(true);
                }
            });
        });
        clearLink.setOnAction(event -> checks.forEach(chk -> chk.setSelected(false)));
        hbox.getChildren().addAll(selectSetLink, clearLink);
        return hbox;
    }

    private void stuffInputs(VBox box, KensaEntry[] entries){
        ObservableList<Node> children = box.getChildren();
        for(KensaEntry entry: entries){
            if( entry == KensaEntry.sep ){
                children.add(new Label(" "));
            } else {
                CheckBox check = new CheckBox(entry.getName());
                check.setWrapText(true);
                check.setUserData(entry);
                checks.add(check);
                children.add(check);
            }
        }
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル ");
        enterButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> onCancel(AddKensaForm.this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        List<String> selected = checks.stream().filter(CheckBox::isSelected)
                .map(ch -> ((KensaEntry)ch.getUserData()).getValue())
                .collect(Collectors.toList());
        onEnter(selected);
    }

    protected void onEnter(List<String> selected){

    }

    protected void onCancel(AddKensaForm form){

    }

}
