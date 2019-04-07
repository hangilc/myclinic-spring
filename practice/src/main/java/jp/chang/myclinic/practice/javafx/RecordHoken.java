package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.javafx.hoken.HokenDisp;
import jp.chang.myclinic.practice.javafx.hoken.HokenSelectForm;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.HokenUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RecordHoken extends StackPane {

    private final int visitId;
    private final int patientId;
    private final LocalDate visitDate;

    public RecordHoken(HokenDTO hoken, VisitDTO visit){
        this.visitId = visit.visitId;
        this.patientId = visit.patientId;
        this.visitDate = DateTimeUtil.parseSqlDateTime(visit.visitedAt).toLocalDate();
        HokenDisp disp = createDisp(hoken);
        getChildren().add(disp);
    }

    public Optional<HokenDisp> findDisp(){
        for(Node node: getChildren()){
            if( node instanceof HokenDisp ){
                return Optional.of((HokenDisp)node);
            }
        }
        return Optional.empty();
    }

    public Optional<HokenSelectForm> findForm(){
        for(Node node: getChildren()){
            if( node instanceof HokenSelectForm ){
                return Optional.of((HokenSelectForm)node);
            }
        }
        return Optional.empty();
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
