package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.DrugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CopySelectedForm extends VBox {

    private List<CheckBox> drugChecks = new ArrayList<>();
    private Pane drugChecksWrapper;
    private TextField daysField = new TextField();
    private CheckBox keepOpenCheck;

    // TODO: support DrugAttrDTO
    CopySelectedForm(List<DrugFullDTO> drugs) {
        super(4);
        PracticeUtil.addFormClass(this);
        getChildren().addAll(
                PracticeUtil.createFormTitle("選択して処方をコピー"),
                createList(drugs),
                createSelectionLinks(),
                createDays(),
                createCommands()
        );
    }

    private Node createList(List<DrugFullDTO> drugs) {
        VBox list = new VBox(2);
        drugs.forEach(drug -> {
            CheckBox check = new CheckBox(DrugUtil.drugRep(drug));
            check.setUserData(drug);
            check.setWrapText(true);
            drugChecks.add(check);
            list.getChildren().add(check);
        });
        drugChecksWrapper = list;
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
        keepOpenCheck = new CheckBox("閉じない");
        enterButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> onClose());
        hbox.getChildren().addAll(enterButton, cancelButton, keepOpenCheck);
        return hbox;
    }

    private boolean isKeepOpen(){
        return keepOpenCheck.isSelected();
    }

    private void doEnter(){
        getDays(days -> {
            List<DrugFullDTO> selectedSrcDrugs = drugChecks.stream()
                    .filter(CheckBox::isSelected)
                    .map(chk -> modifyDrug((DrugFullDTO)chk.getUserData(), days))
                    .collect(Collectors.toList());
            onEnter(selectedSrcDrugs, isKeepOpen());
        });
    }

    private DrugFullDTO modifyDrug(DrugFullDTO src, int days){
        if( days > 0 ){
            DrugCategory category = DrugCategory.fromCode(src.drug.category);
            if( category == DrugCategory.Naifuku || category == DrugCategory.Tonpuku ) {
                DrugFullDTO dst = new DrugFullDTO();
                dst.drug = DrugDTO.copy(src.drug);
                dst.drug.days = days;
                dst.master = src.master;
                return dst;
            } else {
                return src;
            }
        } else {
            return src;
        }
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

    int cleanUpForKeepOpen(){
        List<CheckBox> checked = drugChecks.stream()
                .filter(CheckBox::isSelected).collect(Collectors.toList());
        checked.forEach(chk -> {
            drugChecksWrapper.getChildren().remove(chk);
            drugChecks.remove(chk);
        });
        return drugChecks.size();
    }

    protected void onEnter(List<DrugFullDTO> selected, boolean keepOpen) {

    }

    protected void onClose() {

    }
}
