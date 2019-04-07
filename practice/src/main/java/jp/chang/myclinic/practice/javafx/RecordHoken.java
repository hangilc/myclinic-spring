package jp.chang.myclinic.practice.javafx;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.hoken.HokenDisp;
import jp.chang.myclinic.practice.javafx.hoken.HokenSelectForm;
import jp.chang.myclinic.util.HokenUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecordHoken extends StackPane {

    private final int visitId;
    private final int patientId;
    private final LocalDate visitDate;

    RecordHoken(HokenDTO hoken, VisitDTO visit){
        this.visitId = visit.visitId;
        this.patientId = visit.patientId;
        this.visitDate = LocalDateTime.parse(visit.visitedAt).toLocalDate();
        HokenDisp disp = createDisp(hoken);
        getChildren().add(disp);
    }

    private HokenDisp createDisp(HokenDTO hoken){
        HokenDisp disp = new HokenDisp(hoken);
        disp.setOnClickHandler(() -> {
            Context.frontend.listAvailableHoken(patientId, visitDate)
                    .thenAccept(availableHoken -> {
                        HokenSelectForm form = createForm(availableHoken, hoken, disp);
                        RecordHoken.this.getChildren().setAll(form);
                    });
        });
        return disp;
    }

    private HokenSelectForm createForm(HokenDTO available, HokenDTO current, HokenDisp prevDisp){
        HokenSelectForm form = new HokenSelectForm(visitId, available, current);
        form.setOnCancelHandler(() -> getChildren().setAll(prevDisp));
        form.setOnEnteredHandler(newHoken -> {
            HokenDisp newDisp = createDisp(newHoken);
            getChildren().setAll(newDisp);
        });
        return form;
    }
}
