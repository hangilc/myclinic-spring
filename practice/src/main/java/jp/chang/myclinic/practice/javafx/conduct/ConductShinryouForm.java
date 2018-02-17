package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.practice.javafx.parts.ShinryouSearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

public class ConductShinryouForm extends WorkForm {

    private String at;

    public ConductShinryouForm(String at){
        super("診療行為追加");
        this.at = at;
        ShinryouInput shinryouInput = new ShinryouInput();
        ShinryouSearchBox searchBox = new ShinryouSearchBox(at);
        searchBox.setOnSelectCallback(shinryouInput::setMaster);
        getChildren().addAll(
                shinryouInput,
                searchBox
        );
    }
}
