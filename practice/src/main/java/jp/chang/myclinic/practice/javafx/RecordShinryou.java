package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.events.ShinryouDeletedEvent;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouEditForm;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import jp.chang.myclinic.utilfx.HandlerFX;

class RecordShinryou extends StackPane {

    private ShinryouFullDTO shinryou;
    private ShinryouAttrDTO attr;
    private TextFlow disp;
    private Text labelText = new Text();

    RecordShinryou(ShinryouFullDTO shinryou, ShinryouAttrDTO attr){
        this.shinryou = shinryou;
        this.attr = attr;
        getChildren().add(createDisp());
    }

    private void updateLabelText(){
        String label = shinryou.master.name;
        if( attr != null ){
            if( attr.tekiyou != null ){
                label += " [摘要：" + attr.tekiyou + "]";
            }
        }
        labelText.setText(label);
    }

    private Node createDisp(){
        updateLabelText();
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add("shinryou-disp");
        textFlow.setOnMouseClicked(event -> doMouseClick(attr));
        textFlow.getChildren().add(labelText);
        this.disp = textFlow;
        return disp;
    }

    int getShinryouId() {
        return shinryou.shinryou.shinryouId;
    }

    public int getVisitId() {
        return shinryou.shinryou.visitId;
    }

    public int getShinryoucode() {
        return shinryou.shinryou.shinryoucode;
    }

    private void doMouseClick(ShinryouAttrDTO attr){
        if( PracticeUtil.confirmCurrentVisitAction(shinryou.shinryou.visitId, "診療行為を編集しますか？") ){
            ShinryouEditForm form = new ShinryouEditForm(shinryou, attr){
                @Override
                protected void onDelete(ShinryouEditForm form) {
                    Context.getInstance().getFrontend().deleteShinryou(shinryou.shinryou.shinryouId)
                            .thenAccept(result -> Platform.runLater(() -> {
                                ShinryouDeletedEvent e = new ShinryouDeletedEvent(shinryou.shinryou);
                                RecordShinryou.this.fireEvent(e);
                            }))
                            .exceptionally(HandlerFX::exceptionally);
                }

                @Override
                protected void onCancel(ShinryouEditForm form) {
                    showDisp();
                }

                @Override
                protected void onAttrModified(ShinryouAttrDTO modified) {
                    RecordShinryou.this.attr = modified;
                    updateLabelText();
                }
            };
            getChildren().clear();
            getChildren().add(form);
        }
    }

    private void showDisp(){
        getChildren().clear();
        getChildren().add(disp);
    }

}
