package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.scene.Node;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.parts.CheckBoxList;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.util.List;

public class CopySelectedForm extends WorkForm {

    public CopySelectedForm(List<ShinryouFullDTO> shinryouList){
        super("診療行為のコピー");
        getChildren().addAll(
                createChecks()
        );
    }

    private Node createChecks(List<ShinryouFullDTO> shinryouList){
        CheckBoxList<ShinryouFullDTO> list = new CheckBoxList<ShinryouFullDTO>(s -> s.master.name);
        list.addAll(shinryouList);
    }
}
