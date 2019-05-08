package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.GazouLabelDTO;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class GazouLabel extends StackPane {

    private int conductId;
    private String label;

    GazouLabel(GazouLabelDTO gazouLabelDTO, int conductId) {
        this.conductId = conductId;
        this.label = gazouLabelDTO == null ? "" : gazouLabelDTO.label;
        getChildren().add(createDisp());
    }

    private Node createDisp() {
        return new GazouLabelDisp(label){
            @Override
            protected void onEdit() {
                doEdit(this);
            }
        };
    }

    private void doEdit(GazouLabelDisp disp) {
        GazouLabelForm form = new GazouLabelForm(label) {
            @Override
            protected void onEnter(String value) {
                Context.frontend.setGazouLabel(conductId, value)
                        .thenAccept(result -> Platform.runLater(() -> {
                            GazouLabel.this.label = value;
                            GazouLabel.this.getChildren().setAll(createDisp());
                            onModified(value);
                        }))
                        .exceptionally(HandlerFX.exceptionally(this));
            }

            @Override
            protected void onCancel() {
                GazouLabel.this.getChildren().setAll(disp);
            }

            @Override
            protected void onDelete() {
                if( GuiUtil.confirm("この画像ラベルを消去していいですか？") ){
                    Context.frontend.deleteGazouLabel(conductId)
                            .thenAcceptAsync(result -> {
                                GazouLabel.this.label = "";
                                GazouLabel.this.getChildren().setAll(createDisp());
                                onModified(null);
                           }, Platform::runLater)
                            .exceptionally(HandlerFX.exceptionally(this));
                }
            }
        };
        getChildren().setAll(form);
    }

    abstract protected void onModified(String value);

}
