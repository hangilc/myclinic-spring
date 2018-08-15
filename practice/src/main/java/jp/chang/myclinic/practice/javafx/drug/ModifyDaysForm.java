package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.DrugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ModifyDaysForm extends VBox {

    private List<CheckBox> drugChecks = new ArrayList<>();
    private TextField daysField = new TextField();

    public ModifyDaysForm(List<DrugFullDTO> drugs) {
        super(4);
        PracticeUtil.addFormClass(this);
        getChildren().addAll(
                PracticeUtil.createFormTitle("日数を変更"),
                createList(drugs),
                createSelectionLinks(),
                createDays(),
                createCommands()
        );
    }

    private Node createList(List<DrugFullDTO> drugs) {
        VBox list = new VBox(2);
        drugs.forEach(drug -> {
            DrugCategory category = DrugCategory.fromCode(drug.drug.category);
            if( category == DrugCategory.Naifuku ){
                CheckBox check = new CheckBox(DrugUtil.drugRep(drug));
                check.setUserData(drug);
                check.setWrapText(true);
                drugChecks.add(check);
                list.getChildren().add(check);
            }
        });
        return list;
    }

    private Node createSelectionLinks(){
        HBox hbox = new HBox(4);
        Hyperlink selectAllLink = new Hyperlink("全部選択");
        Hyperlink unselectAllLink = new Hyperlink("全部解除");
        selectAllLink.setOnAction(event -> drugChecks.forEach(chk -> chk.setSelected(true)));
        unselectAllLink.setOnAction(event -> drugChecks.forEach(chk -> chk.setSelected(false)));
        hbox.getChildren().addAll(selectAllLink, unselectAllLink);
        return hbox;
    }

    private Node createDays(){
        HBox hbox = new HBox(4);
        daysField.getStyleClass().add("days-field");
        hbox.getChildren().addAll(new Label("日数："), daysField, new Label("日分"));
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> onClose());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    private void doEnter(){
        getDays(days -> {
            List<DrugDTO> drugs = drugChecks.stream()
                    .filter(CheckBox::isSelected)
                    .map(chk -> ((DrugFullDTO)chk.getUserData()).drug)
                    .collect(Collectors.toList());
            onEnter(drugs, days);
        });
    }

    private void getDays(Consumer<Integer> cb){
        String daysInput = daysField.getText();
        if( daysInput.isEmpty() ){
            cb.accept(0);
        } else {
            try {
                int days = Integer.parseInt(daysField.getText());
                if( days <= 0 ){
                    GuiUtil.alertError("日数の入力が不適切です。");
                } else {
                    cb.accept(days);
                }
            } catch(NumberFormatException ex){
                GuiUtil.alertError("日数の入力が不適切です。");
            }
        }
    }

    protected void onEnter(List<DrugDTO> drugs, int days) {

    }

    protected void onClose() {

    }
}
