package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.dto.ShinryouFullWithAttrDTO;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxList;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.util.List;

abstract public class HandleSelectedForm extends WorkForm {

    private CheckBoxList<ShinryouFullWithAttrDTO> checkInputs;
    private Runnable onCancelHandler = () -> {};

    public HandleSelectedForm(String title, List<ShinryouFullWithAttrDTO> shinryouList){
        super(title);
        getChildren().addAll(
                createChecks(shinryouList),
                createSelectionLinks(),
                createCommands()
        );
    }

    public void setOnCancelHandler(Runnable onCancelHandler) {
        this.onCancelHandler = onCancelHandler;
    }

    private Node createChecks(List<ShinryouFullWithAttrDTO> shinryouList){
        checkInputs = new CheckBoxList<>(shinryouList, this::createLabel);
        return checkInputs;
    }

    private String createLabel(ShinryouFullWithAttrDTO shinryou){
        String name = shinryou.shinryou.master.name;
        String tekiyou = ShinryouAttrDTO.extractTekiyou(shinryou.attr);
        if( tekiyou != null && !tekiyou.isEmpty() ){
            name += String.format(" [%s]", tekiyou);

        }
        return name;
    }

    private Node createSelectionLinks(){
        HBox hbox = new HBox(4);
        Hyperlink selectAllLink = new Hyperlink("全部選択");
        Hyperlink unselectAllLink = new Hyperlink("全部解除");
        selectAllLink.setOnAction(evt -> {
            checkInputs.forEachCheckBox(chk -> chk.setSelected(true));
        });
        unselectAllLink.setOnAction(evt -> {
            checkInputs.forEachCheckBox(chk -> chk.setSelected(false));
        });
        hbox.getChildren().addAll(selectAllLink, unselectAllLink);
        return hbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(event -> onEnter(checkInputs.getSelected()));
        cancelButton.setOnAction(event -> onCancelHandler.run());
        hbox.getChildren().addAll(enterButton, cancelButton);
        return hbox;
    }

    abstract protected void onEnter(List<ShinryouFullWithAttrDTO> selection);

}
