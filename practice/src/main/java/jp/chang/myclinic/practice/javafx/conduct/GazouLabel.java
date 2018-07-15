package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import jp.chang.myclinic.dto.GazouLabelDTO;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GazouLabel extends StackPane {

    private static Logger logger = LoggerFactory.getLogger(GazouLabel.class);
    private int conductId;
    private String label;

    public GazouLabel(GazouLabelDTO gazouLabelDTO, int conductId) {
        this.conductId = conductId;
        this.label = gazouLabelDTO == null ? "" : gazouLabelDTO.label;
        getChildren().add(createDisp());
    }

    private Node createDisp() {
        GazouLabelDisp disp = new GazouLabelDisp(label){
            @Override
            protected void onEdit() {
                doEdit(this);
            }
        };
        return disp;
    }

    private void doEdit(GazouLabelDisp disp) {
        GazouLabelForm form = new GazouLabelForm(label) {
            @Override
            protected void onEnter(String value) {
                Service.api.modifyGazouLabel(conductId, value)
                        .thenAccept(result -> Platform.runLater(() -> {
                            GazouLabel.this.label = value;
                            GazouLabel.this.getChildren().setAll(createDisp());
                            onModified(value);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }

            @Override
            protected void onCancel() {
                GazouLabel.this.getChildren().setAll(disp);
            }
        };
        getChildren().setAll(form);
    }

    protected void onModified(String value){

    }

}
