package jp.chang.myclinic.practice.javafx.conduct;

import jp.chang.myclinic.practice.javafx.parts.WorkForm;

public class ConductShinryouForm extends WorkForm {

    public ConductShinryouForm(){
        super("診療行為追加");
        ShinryouInput shinryouInput = new ShinryouInput();
        add(shinryouInput);
    }
}
