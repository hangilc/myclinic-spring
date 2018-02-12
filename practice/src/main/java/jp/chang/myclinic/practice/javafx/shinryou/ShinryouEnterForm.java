package jp.chang.myclinic.practice.javafx.shinryou;

import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;
import jp.chang.myclinic.practice.javafx.parts.ShinryouForm;

class ShinryouEnterForm extends ShinryouForm {

    private int visitId;

    ShinryouEnterForm(String at, int visitId){
        super("診療行為検索", at);
        this.visitId = visitId;
    }

    @Override
    protected void onEnter(int shinryoucode) {
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visitId;
        shinryou.shinryoucode = shinryoucode;
        FunJavaFX.INSTANCE.enterShinryou(shinryou, entered -> {
            ShinryouEnterForm.this.fireEvent(new ShinryouEnteredEvent(entered));
            onEntered(this);
        });
    }

    protected void onEntered(ShinryouEnterForm form){

    }
}
