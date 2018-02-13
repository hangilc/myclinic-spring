package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxList;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.util.List;

public class CopySelectedForm extends WorkForm {

    private CheckBoxList<ShinryouFullDTO> checkInputs;

    public CopySelectedForm(List<ShinryouFullDTO> shinryouList){
        super("診療行為のコピー");
        getChildren().addAll(
                createChecks(shinryouList),
                createSelectionLinks(),
                createCommands()
        );
    }

    private Node createChecks(List<ShinryouFullDTO> shinryouList){
        checkInputs = new CheckBoxList<ShinryouFullDTO>(shinryouList, s -> s.master.name);
        return checkInputs;
    }

    private Node createSelectionLinks(){
        HBox hbox = new HBox(4);
        Hyperlink selectAllLink = new Hyperlink("全部選択");
        Hyperlink unselectAllLink = new Hyperlink("全部解除");
        hbox.getChildren().addAll(selectAllLink, unselectAllLink);
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(event -> onEnter(this, checkInputs.getSelected()));
        cancelButton.setOnAction(event -> onCancel(this));
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    protected void onEnter(CopySelectedForm form, List<ShinryouFullDTO> selection){

    }

    protected void onCancel(CopySelectedForm form){

    }
}
