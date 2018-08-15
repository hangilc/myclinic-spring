package jp.chang.myclinic.practice.javafx.disease;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.DiseaseFullDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.practice.javafx.disease.select.Disp;
import jp.chang.myclinic.practice.javafx.disease.select.SelectionList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public class Select extends VBox {

    private static Logger logger = LoggerFactory.getLogger(Select.class);
    private Disp disp;
    private Consumer<DiseaseFullDTO> onSelectCallback = d -> {};

    public Select(List<DiseaseFullDTO> diseases) {
        super(4);
        getChildren().addAll(
                createDisp(),
                createCommands(),
                createList(diseases)
        );
    }

    public void setOnSelectCallback(Consumer<DiseaseFullDTO> onSelectCallback) {
        this.onSelectCallback = onSelectCallback;
    }

    private Node createDisp(){
        this.disp = new Disp();
        return disp;
    }

    private Node createCommands(){
        Button editButton = new Button("編集");
        editButton.setOnAction(evt -> doEdit());
        return editButton;
    }

    private Node createList(List<DiseaseFullDTO> diseases){
        SelectionList list = new SelectionList();
        list.setResult(diseases);
        list.setOnSelectCallback(disp::setDisease);
        return list;
    }

    private void doEdit(){
        DiseaseFullDTO disease = disp.getDisease();
        if( disease == null ){
            GuiUtil.alertError("病名が選択されていません。");
        } else {
            onSelectCallback.accept(disease);
        }
    }

}
