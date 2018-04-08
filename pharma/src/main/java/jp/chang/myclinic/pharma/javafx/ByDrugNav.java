package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.IyakuhincodeNameDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

abstract class ByDrugNav extends VBox implements Nav {

    private static Logger logger = LoggerFactory.getLogger(ByDrugNav.class);
    private int currentPage;
    private int totalPages;
    private int patientId;
    private VBox workarea = new VBox(4);

    ByDrugNav(int patientId, String patientName) {
        this.patientId = patientId;
        getChildren().addAll(
                new Label("(" + patientName + ")"),
                workarea
        );
    }

    void showSummary(){
        Service.api.listIyakuhinForPatient(patientId)
                .thenAccept(result -> Platform.runLater(() -> {
                    renderSummary(result);
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private void renderSummary(List<IyakuhincodeNameDTO> items){
        workarea.getChildren().clear();
        items.forEach(item -> {
            Hyperlink link = new Hyperlink(item.name);
            workarea.getChildren().add(new HBox(0, link));
        });
    }

    @Override
    public void trigger() {

    }
}
