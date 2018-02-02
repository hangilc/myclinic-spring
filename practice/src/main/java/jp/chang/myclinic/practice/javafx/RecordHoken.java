package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.HokenUtil;

public class RecordHoken extends StackPane {

    private int visitId;
    private int patientId;
    private String visitedAt;

    public RecordHoken(HokenDTO hoken, VisitDTO visit){
        this.visitId = visit.visitId;
        this.patientId = visit.patientId;
        this.visitedAt = visit.visitedAt;
        showDisp(hoken);
    }

    private void showDisp(HokenDTO hoken){
        getChildren().clear();
        getChildren().add(createDisp(hoken));
    }

    private Node createDisp(HokenDTO hoken){
        TextFlow disp = new TextFlow();
        disp.setOnMouseClicked(event -> {
            PracticeLib.listAvailableHoken(patientId, visitedAt, (HokenDTO available) -> {
                HokenSelectForm form = new HokenSelectForm(available, hoken);
                form.setCallback(new HokenSelectForm.Callback() {
                    @Override
                    public void onEnter(VisitDTO newVisit) {
                        newVisit.visitId = visitId;
                        PracticeLib.updateHoken(newVisit, newHoken -> showDisp(newHoken));
                    }

                    @Override
                    public void onCancel() {
                        getChildren().clear();
                        getChildren().add(disp);
                    }
                });
                getChildren().clear();
                getChildren().add(form);
            });
        });
        disp.getChildren().add(new Text(makeDispText(hoken)));
        return disp;
    }

    private String makeDispText(HokenDTO hoken){
        String rep = HokenUtil.hokenRep(hoken);
        if (rep.isEmpty()) {
            rep = "(保険なし)";
        }
        return rep;
    }

}
