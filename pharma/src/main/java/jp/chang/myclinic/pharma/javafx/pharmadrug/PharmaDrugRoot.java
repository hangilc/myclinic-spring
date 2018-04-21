package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;

class PharmaDrugRoot extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(PharmaDrugRoot.class);
    private PharmaDrugList pharmaDrugList = new PharmaDrugList();
    private Edit edit;

    PharmaDrugRoot() {
        super(4);
        pharmaDrugList.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
           if( newValue != null ){
               edit.setPharmaDrug(newValue);
           }
        });
        edit = new Edit(){
            @Override
            void onEnter(int iyakuhincode) {
                reloadList();
            }

            @Override
            void onDelete(int iyakuhincode) {
                reloadList();
            }
        };
        getChildren().addAll(pharmaDrugList, edit);
        reloadList();
    }

    void reloadList() {
        Service.api.listAllPharmaDrugNames()
                .thenAccept(result -> Platform.runLater(() -> pharmaDrugList.getItems().setAll(result)))
                .exceptionally(HandlerFX::exceptionally);
    }

}
