package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

public class ConductShinryouForm extends WorkForm {

    public ConductShinryouForm(){
        super("診療行為追加");
        ShinryouInput shinryouInput = new ShinryouInput();
        SearchBox<ShinryouMasterDTO> searchBox = new SearchBox<>();
        getChildren().addAll(
                shinryouInput,
                searchBox
        );
    }
}
