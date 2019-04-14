package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;

import java.time.LocalDate;

class ShinryouEnterForm extends ShinryouForm {

    private int visitId;

    ShinryouEnterForm(int visitId, LocalDate at) {
        super("診療行為検索", at);
        this.visitId = visitId;
    }

    @Override
    protected void onEnter(int shinryoucode) {
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visitId;
        shinryou.shinryoucode = shinryoucode;
        class Local {
            private ShinryouFullDTO entered;
        }
        Local local = new Local();
        Context.frontend.enterShinryou(shinryou)
                .thenCompose(Context.frontend::getShinryouFull)
                .thenCompose(entered -> {
                    local.entered = entered;
                    return Context.frontend.getShinryouAttr(entered.shinryou.shinryouId);
                })
                .thenAccept(attr -> {
                    Platform.runLater(() -> {
                        ShinryouEnterForm.this.fireEvent(new ShinryouEnteredEvent(local.entered, attr));
                        onEntered(this);
                    });
                })
                .exceptionally(ex -> {
                    FunJavaFX.createErrorHandler().accept(ex);
                    return null;
                });
    }

    protected void onEntered(ShinryouEnterForm form) {

    }
}
