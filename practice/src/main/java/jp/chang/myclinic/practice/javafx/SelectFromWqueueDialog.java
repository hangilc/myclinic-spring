package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SelectFromWqueueDialog extends Stage {

    private WqueueTable wqueueTable;
    private Button selectButton;

    public SelectFromWqueueDialog(List<WqueueFullDTO> list){
        setTitle("受付患者選択");
        VBox root = new VBox(4);
        root.setStyle("-fx-padding: 10");
        wqueueTable = createWqueueTable(list);
        wqueueTable.setOnMouseClicked(event -> {
            if( event.getButton().equals(MouseButton.PRIMARY) ){
                if( event.getClickCount() == 2 ){
                    doSelect();
                }
            }
        });
        root.getChildren().addAll(
                wqueueTable,
                createButtons()
        );
        setScene(new Scene(root));
    }

    public List<WqueueFullDTO> getList(){
        return wqueueTable.getItems();
    }

    public boolean simulateSelectVisit(int visitId){
        WqueueFullDTO wq = null;
        for(WqueueFullDTO item: wqueueTable.getItems()){
            if( item.visit.visitId == visitId ){
                wq = item;
                break;
            }
        }
        if( wq == null ){
            return false;
        } else {
            wqueueTable.getSelectionModel().select(wq);
            return true;
        }
    }

    public void simulateSelectButtonClick(){
        selectButton.fire();
    }

    private WqueueTable createWqueueTable(List<WqueueFullDTO> list){
        return new WqueueTable(list);
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        this.selectButton = new Button("選択");
        Button closeButton = new Button("閉じる");
        selectButton.setOnAction(event -> doSelect());
        closeButton.setOnAction(event -> close());
        hbox.getChildren().addAll(selectButton, closeButton);
        return hbox;
    }

    private CompletableFuture<Void> suspendPatient(){
        int currentVisitId = Context.currentPatientService.getCurrentVisitId();
        if( currentVisitId > 0 ){
            return Context.frontend.suspendExam(currentVisitId)
                    .thenAccept(v -> Context.currentPatientService.setCurrentPatient(null, 0));
        } else {
            return CompletableFuture.completedFuture(null);
        }

    }

    private void doSelect(){
        WqueueFullDTO wq = wqueueTable.getSelectionModel().getSelectedItem();
        if( wq != null ){
            Frontend frontend = Context.frontend;
            suspendPatient()
                    .thenCompose(v -> frontend.startExam(wq.visit.visitId))
                    .thenCompose(v -> frontend.listVisitFull2(wq.patient.patientId, 0))
                    .thenAcceptAsync(result -> {
                        Context.currentPatientService.setCurrentPatient(wq.patient, wq.visit.visitId);
                        Context.integrationService.broadcastVisitPage(
                                result.page, result.totalPages, result.visits
                        );
                        close();
                    }, Platform::runLater)
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
