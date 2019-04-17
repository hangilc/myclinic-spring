package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.ConductFullDTO;
import jp.chang.myclinic.dto.GazouLabelDTO;
import jp.chang.myclinic.practice.javafx.conduct.ConductEditForm;
import jp.chang.myclinic.practice.lib.PracticeUtil;

import java.time.LocalDate;

class RecordConduct extends StackPane {

    private LocalDate at;
    private int conductId;
    private Runnable onDeletedHandler = () -> {};

    RecordConduct(ConductFullDTO conduct, LocalDate at){
        this.at = at;
        this.conductId = conduct.conduct.conductId;
        getChildren().add(createDisp(conduct));
    }

    public int getConductId() {
        return conductId;
    }

    public void setOnDeletedHandler(Runnable onDeletedHandler) {
        this.onDeletedHandler = onDeletedHandler;
    }

    private Node createDisp(ConductFullDTO conduct){
        VBox disp = new VBox();
        String kindText = ConductKind.codeToKanjiRep(conduct.conduct.kind);
        disp.getChildren().add(new Label("<" + kindText + ">"));
        GazouLabelDTO gazouLabel = conduct.gazouLabel;
        if( gazouLabel != null && gazouLabel.label != null && !gazouLabel.label.isEmpty() ){
            disp.getChildren().add(new Label(gazouLabel.label));
        }
        if( conduct.conductShinryouList != null ){
            conduct.conductShinryouList.forEach(shinryou -> {
                disp.getChildren().add(new RecordConductShinryou(shinryou));
            });
        }
        if( conduct.conductDrugs != null ){
            conduct.conductDrugs.forEach(drug -> {
                disp.getChildren().add(new RecordConductDrug(drug));
            });
        }
        if( conduct.conductKizaiList != null ){
            conduct.conductKizaiList.forEach(kizai -> {
                disp.getChildren().add(new RecordConductKizai(kizai));
            });
        }
        disp.setOnMouseClicked(evt -> onClick(conduct));
        return disp;
    }

    private void onClick(ConductFullDTO conduct){
        if( PracticeUtil.confirmCurrentVisitAction(conduct.conduct.visitId, "処置を編集しますか？") ){
            ConductEditForm form = new ConductEditForm(conduct, at);
            form.setOnCloseHandler(c -> setContent(createDisp(c)));
            form.setOnDeletedHandler(() -> onDeletedHandler.run());
            setContent(form);
        }
    }

    private void setContent(Node content){
        getChildren().clear();
        getChildren().add(content);
    }

}
