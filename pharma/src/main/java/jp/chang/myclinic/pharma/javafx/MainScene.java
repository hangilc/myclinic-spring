package jp.chang.myclinic.pharma.javafx;

import javafx.application.Platform;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import jp.chang.myclinic.pharma.javafx.event.StartPrescEvent;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class MainScene extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    @Autowired
    private LeftColumn leftColumn;
    @Autowired
    private RightColumn rightColumn;

    public MainScene() {
        super(4);
        getStyleClass().add("main-scene");
        addEventHandler(StartPrescEvent.eventType, this::onStartPresc);
//        leftColumn = new LeftColumn(){
//            @Override
//            protected void onStartPresc(int visitId) {
//                class Local { private PharmaQueueFullDTO item; }
//                Local local = new Local();
//                Service.api.getPharmaQueueFull(visitId)
//                        .thenCompose(item -> {
//                            local.item = item;
//                            return Service.api.listDrugFull(item.visitId);
//                        })
//                        .thenAccept(drugs -> Platform.runLater(() -> {
//                            rightColumn.startPresc(local.item, drugs);
//                        }))
//                        .exceptionally(HandlerFX::exceptionally);
//            }
//        };
//        rightColumn = new RightColumn(){
//            @Override
//            void onCancel() {
//                leftColumn.clearSelection();
//            }
//
//            @Override
//            void onPrescDone() {
//                //scope.reloadPatientList();
//            }
//        };
    }

    @PostConstruct
    public void postConstruct(){
        ScrollPane rightScroll = new ScrollPane(rightColumn);
        rightScroll.getStyleClass().add("right-column-scroll");
        rightScroll.setFitToWidth(true);
        getChildren().addAll(leftColumn, rightScroll);
    }

//    public void onVisitCreated(Visit created, Runnable toNext) {
//        leftColumn.addVisit(created);
//        toNext.run();
//    }
//
//    public void onVisitDeleted(int visitId, Runnable toNext) {
//        leftColumn.deleteVisit(visitId);
//        toNext.run();
//    }
//
//    public void onPharmaQueueCreated(Visit visit, Runnable toNext) {
//        leftColumn.addPharmaQueue(visit);
//        toNext.run();
//    }
//
//    public void onPharmaQueueDeleted(int visitId, Runnable toNext) {
//        leftColumn.deletePharmaQueue(visitId);
//        toNext.run();
//    }

    private void onStartPresc(StartPrescEvent event){
        int visitId = event.getVisitId();
        class Local {
            private PharmaQueueFullDTO item;
        }
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

}
