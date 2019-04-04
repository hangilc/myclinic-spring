package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.ChargeDTO;
import jp.chang.myclinic.practice.javafx.charge.ChargeForm;
import jp.chang.myclinic.utilfx.HandlerFX;

class RecordCharge extends StackPane {

    private ChargeDTO chargeDTO;

    RecordCharge(ChargeDTO chargeDTO) {
        this.chargeDTO = chargeDTO;
        getChildren().add(createDisp());
    }

    private Node createDisp(){
        TextFlow textFlow = new TextFlow(new Text(getChargeText(chargeDTO)));
        textFlow.setOnMouseClicked(evt -> onMouseClick(textFlow));
        return textFlow;
    }

    private void onMouseClick(Node disp){
        if( chargeDTO != null ){
            Context.frontend.getMeisai(chargeDTO.visitId)
                    .thenAccept(meisai -> Platform.runLater(() -> {
                        ChargeForm form = new ChargeForm(meisai, chargeDTO) {
                            @Override
                            protected void onEnter(int chargeValue) {
                                Context.frontend.endExam(chargeDTO.visitId, chargeValue)
                                        .thenAccept(result -> Platform.runLater(() -> {
                                            chargeDTO = ChargeDTO.copy(chargeDTO);
                                            chargeDTO.charge = chargeValue;
                                            RecordCharge.this.getChildren().setAll(createDisp());
                                        }))
                                        .exceptionally(HandlerFX::exceptionally);
                            }

                            @Override
                            protected void onCancel() {
                                RecordCharge.this.getChildren().setAll(disp);
                            }
                        };
                        getChildren().setAll(form);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private String getChargeText(ChargeDTO charge){
        String label;
        if( charge != null ){
            label = String.format("請求額：%,d円", charge.charge);
        } else {
            label = "(未請求)";
        }
        return label;
    }

}
