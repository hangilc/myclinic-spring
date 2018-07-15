package jp.chang.myclinic.practice.javafx.drug;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DrugDTO;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.util.DrugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DeleteSelectedForm extends VBox {
    private List<CheckBox> drugChecks = new ArrayList<>();
    private TextField daysField = new TextField();

    public DeleteSelectedForm(List<DrugFullDTO> drugs) {
        super(4);
        PracticeUtil.addFormClass(this);
        getChildren().addAll(
                PracticeUtil.createFormTitle("薬剤の複数削除"),
                createList(drugs),
                createSelectionLinks(),
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
        return list;
    }

    private Node createSelectionLinks() {
        HBox hbox = new HBox(4);
        Hyperlink selectAllLink = new Hyperlink("全部選択");
        Hyperlink unselectAllLink = new Hyperlink("全部解除");
        selectAllLink.setOnAction(event -> drugChecks.forEach(chk -> chk.setSelected(true)));
        unselectAllLink.setOnAction(event -> drugChecks.forEach(chk -> chk.setSelected(false)));
        hbox.getChildren().addAll(selectAllLink, unselectAllLink);
        return hbox;
    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        Button deleteButton = new Button("削除");
        Button cancelButton = new Button("キャンセル");
        deleteButton.setOnAction(event -> doDelete());
        cancelButton.setOnAction(event -> onClose());
        hbox.getChildren().addAll(deleteButton, cancelButton);
        return hbox;
    }

    private void doDelete() {
        List<DrugDTO> drugs = drugChecks.stream()
                .filter(CheckBox::isSelected)
                .map(chk -> ((DrugFullDTO) chk.getUserData()).drug)
                .collect(Collectors.toList());
        onDelete(drugs);
    }

    protected void onDelete(List<DrugDTO> drugs) {

    }

    protected void onClose() {

    }

}
