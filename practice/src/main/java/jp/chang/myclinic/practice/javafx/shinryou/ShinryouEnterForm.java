package jp.chang.myclinic.practice.javafx.shinryou;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouDTO;
import jp.chang.myclinic.dto.ShinryouFullDTO;
import jp.chang.myclinic.practice.javafx.FunJavaFX;
import jp.chang.myclinic.practice.javafx.events.ShinryouEnteredEvent;

class ShinryouEnterForm extends ShinryouForm {

    private int visitId;

    ShinryouEnterForm(String at, int visitId) {
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
        Context.getInstance().getFrontend().enterShinryou(shinryou)
                .thenCompose(Context.getInstance().getFrontend()::getShinryouFull)
                .thenCompose(entered -> {
                    local.entered = entered;
                    return Context.getInstance().getFrontend().findShinryouAttr(entered.shinryou.shinryouId);
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
