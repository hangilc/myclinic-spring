package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainScene extends HBox {

    private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    private LeftColumn leftColumn;
    private RightColumn rightColumn;

    public MainScene() {
        super(4);
        getStyleClass().add("main-scene");
        leftColumn = new LeftColumn(){
            @Override
            protected void onPatientSelected(PharmaQueueFullDTO item) {
                Service.api.listDrugFull(item.visitId)
                        .thenAccept(drugs -> Platform.runLater(() -> {
                            rightColumn.startPresc(item, drugs);
                        }))
                        .exceptionally(HandlerFX::exceptionally);
            }
        };
        rightColumn = new RightColumn(){
            @Override
            void onCancel() {
                leftColumn.clearSelection();
            }

            @Override
            void onPrescDone() {
                leftColumn.reloadPatientList();
            }
        };
        ScrollPane rightScroll = new ScrollPane(rightColumn);
        rightScroll.getStyleClass().add("right-column-scroll");
        rightScroll.setFitToWidth(true);
        getChildren().addAll(leftColumn, rightScroll);
    }

}
