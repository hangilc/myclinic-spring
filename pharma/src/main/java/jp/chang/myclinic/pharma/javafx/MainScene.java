package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.Scope;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import jp.chang.myclinic.pharma.tracker.DispatchHook;
import jp.chang.myclinic.pharma.tracker.model.PharmaQueue;
import jp.chang.myclinic.pharma.tracker.model.Wqueue;

public class MainScene extends HBox implements DispatchHook {

    //private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    private LeftColumn leftColumn;
    private RightColumn rightColumn;

    public MainScene(Scope scope) {
        super(4);
        getStyleClass().add("main-scene");
        leftColumn = new LeftColumn(scope){
            @Override
            protected void onStartPresc(int visitId) {
                class Local { private PharmaQueueFullDTO item; }
                Local local = new Local();
                Service.api.getPharmaQueueFull(visitId)
                        .thenCompose(item -> {
                            local.item = item;
                            return Service.api.listDrugFull(item.visitId);
                        })
                        .thenAccept(drugs -> Platform.runLater(() -> {
                            rightColumn.startPresc(local.item, drugs);
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
                scope.reloadPatientList();
            }
        };
        ScrollPane rightScroll = new ScrollPane(rightColumn);
        rightScroll.getStyleClass().add("right-column-scroll");
        rightScroll.setFitToWidth(true);
        getChildren().addAll(leftColumn, rightScroll);
    }

    @Override
    public void onWqueueCreated(Wqueue created, Runnable toNext) {
        leftColumn.addWqueue(created);
        System.out.println("wqueue created: " + created);
        toNext.run();
    }

    @Override
    public void onPharmaQueueCreated(PharmaQueue created, Runnable toNext) {
        leftColumn.addPharmaQueue(created);
        toNext.run();
    }

    @Override
    public void onPharmaQueueDeleted(int visitId, Runnable toNext) {
        leftColumn.deletePharmaQueue(visitId);
        toNext.run();
    }
}
