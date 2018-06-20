package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class PharmaDrugRoot extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(PharmaDrugRoot.class);
    private PharmaDrugList pharmaDrugList = new PharmaDrugList();
    private Edit edit;
    private String filterText = null;
    private List<PharmaDrugNameDTO> allPharmaDrugs = Collections.emptyList();

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
        getChildren().addAll(createListPane(), edit);
        reloadList();
    }

    private Node createListPane(){
        VBox vbox = new VBox(4);
        TextField textField = new TextField();
        Button filterButton = new Button("絞込み");
        Button resetButton = new Button("解除");
        filterButton.setOnAction(evt -> {
            filterText = textField.getText();
            updateList();
        });
        resetButton.setOnAction(evt -> {
            filterText = null;
            updateList();
            textField.setText("");
        });
        vbox.getChildren().addAll(
                new HBox(4, textField, filterButton, resetButton),
                pharmaDrugList
        );
        return vbox;
    }

    private void updateList(){
        List<PharmaDrugNameDTO> list = allPharmaDrugs;
        if( filterText != null && !filterText.isEmpty() ){
            list = list.stream()
                    .filter(item -> item.name.contains(filterText))
                    .collect(Collectors.toList());
        }
        pharmaDrugList.getItems().setAll(list);
    }

    private void reloadList() {
        Service.api.listAllPharmaDrugNames()
                .thenAccept(result -> Platform.runLater(() -> {
                    allPharmaDrugs = result;
                    updateList();
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

}
