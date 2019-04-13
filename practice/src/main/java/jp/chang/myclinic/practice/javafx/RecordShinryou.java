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
    private ShinryouAttrDTO attr;

    RecordShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        this.shinryou = shinryou.shinryou;
        this.attr = ShinryouAttrDTO.copy(attr);
        getChildren().add(createDisp(shinryou, attr));
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
//            {
//                @Override
//                protected void onDelete(ShinryouEditForm form) {
//                    Context.frontend.deleteShinryou(shinryou.shinryou.shinryouId)
//                            .thenAccept(result -> Platform.runLater(() -> {
//                                ShinryouDeletedEvent e = new ShinryouDeletedEvent(shinryou.shinryou);
//                                RecordShinryou.this.fireEvent(e);
//                            }))
//                            .exceptionally(HandlerFX::exceptionally);
//                }
//
//                @Override
//                protected void onCancel(ShinryouEditForm form) {
//                    getChildren().setAll(disp);
//                }
//
//                @Override
//                protected void onAttrModified(ShinryouAttrDTO modified) {
//                    RecordShinryou.this.attr = modified;
//                    getChildren().setAll(createDisp(shinryou, attr));
//                }
//            };
            getChildren().setAll(form);
        }
    }

}
