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

    private VisitDTO visit;

    public RecordHoken(HokenDTO hoken, VisitDTO visit){
        this.visit = visit;
        getChildren().add(createDisp(hoken));
    }

    private Node createDisp(HokenDTO hoken){
        TextFlow disp = new TextFlow();
        disp.setOnMouseClicked(event -> {
            PracticeLib.listAvailableHoken(visit, available -> {
                HokenSelectForm form = new HokenSelectForm(hoken, available);
                form.setCallback(new HokenSelectForm.Callback() {
                    @Override
                    public void onEnter(VisitDTO visit) {

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
