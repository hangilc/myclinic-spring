package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.events.ShinryouDeletedEvent;
import jp.chang.myclinic.practice.javafx.shinryou.ShinryouEditForm;
import jp.chang.myclinic.practice.lib.PracticeUtil;

class RecordShinryou extends StackPane {

    private int shinryouId;
    private int visitId;
    private int shinryoucode;
    private Node disp;

    RecordShinryou(ShinryouFullDTO shinryou){
        this.shinryouId = shinryou.shinryou.shinryouId;
        this.visitId = shinryou.shinryou.visitId;
        this.shinryoucode = shinryou.shinryou.shinryoucode;
        this.disp = createDisp(shinryou);
        getChildren().add(disp);
    }

    private Node createDisp(ShinryouFullDTO shinryou){
        String label = shinryou.master.name;
        Text text = new Text(label);
        TextFlow textFlow = new TextFlow();
        textFlow.getStyleClass().add("shinryou-disp");
        textFlow.setOnMouseClicked(event -> doMouseClick(shinryou));
        textFlow.getChildren().add(text);
        this.disp = textFlow;
        return textFlow;
    }

    public int getShinryouId() {
        return shinryouId;
    }

    public int getVisitId() {
        return visitId;
    }

    public int getShinryoucode() {
        return shinryoucode;
    }

    private void doMouseClick(ShinryouFullDTO shinryou){
        if( PracticeUtil.confirmCurrentVisitAction(visitId, "診療行為を編集しますか？") ){
            ShinryouEditForm form = new ShinryouEditForm(shinryou){
                @Override
                protected void onDelete(ShinryouEditForm form) {
                    Service.api.deleteShinryou(shinryou.shinryou.shinryouId)
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
