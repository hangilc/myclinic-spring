package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouDisp;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouEditForm;
import jp.chang.myclinic.practice.lib.PracticeUtil;

class RecordShinryou extends StackPane {

    private ShinryouDTO shinryou;
    private Runnable onDeletedHandler = () -> {};

    RecordShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        this.shinryou = shinryou.shinryou;
        getChildren().add(createDisp(shinryou, attr));
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    private Node createDisp(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        ShinryouDisp disp = new ShinryouDisp(shinryou.master.name, attr);
        disp.setOnMouseClicked(evt -> doMouseClick(disp, shinryou, attr));
        return disp;
    }

    int getShinryouId() {
        return shinryou.shinryouId;
    }

    public int getVisitId() {
        return shinryou.visitId;
    }

    public int getShinryoucode() {
        return shinryou.shinryoucode;
    }

    private void doMouseClick(ShinryouDisp disp, ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        if( PracticeUtil.confirmCurrentVisitAction(shinryou.shinryou.visitId, "診療行為を編集しますか？") ){
            ShinryouEditForm form = new ShinryouEditForm(shinryou, attr);
            form.setOnEnteredHandler(updatedAttr -> {
                getChildren().setAll(createDisp(shinryou, updatedAttr));
            });
            form.setOnCancelHandler(() -> getChildren().setAll(disp));
            form.setOnDeletedHandler(onDeletedHandler);
            getChildren().setAll(form);
        }
    }

}
