package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import jp.chang.myclinic.pharma.Service;
import jp.chang.myclinic.pharma.javafx.lib.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class PharmaDrugListDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(PharmaDrugListDialog.class);

    PharmaDrugListDialog(List<PharmaDrugNameDTO> items) {
        HBox root = new HBox(4);
        root.getStylesheets().add("Pharma.css");
        root.getStyleClass().add("pharma-drug-list-dialog");
        ListView<PharmaDrugNameDTO> listView = createListView(items);
        root.getChildren().addAll(
                listView,
                createCommands(listView)
        );
        setScene(new Scene(root));
    }

    private ListView<PharmaDrugNameDTO> createListView(List<PharmaDrugNameDTO> items) {
        ListView<PharmaDrugNameDTO> view = new ListView<>();
        view.setCellFactory(listView -> new ListCell<PharmaDrugNameDTO>() {
            @Override
            protected void updateItem(PharmaDrugNameDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.name);
                }
            }
        });
        view.getItems().setAll(items);
        view.setOnMouseClicked(event -> {
            if( event.getClickCount() == 2 ){
                doSelect(view);
            }
        });
        return view;
    }

    private Node createCommands(ListView<PharmaDrugNameDTO> listView){
        HBox hbox = new HBox(4);
        Button selectButton = new Button("選択");
        Button cancelButton = new Button("キャンセル");
        selectButton.setOnAction(evt -> doSelect(listView));
        cancelButton.setOnAction(evt -> onCancel());
        hbox.getChildren().addAll(selectButton, cancelButton);
        return hbox;
    }

    private void doSelect(ListView<PharmaDrugNameDTO> listView){
        PharmaDrugNameDTO item = listView.getSelectionModel().getSelectedItem();
        if( item != null ){
            Service.api.getPharmaDrug(item.iyakuhincode)
                    .thenAccept(pharmaDrug -> Platform.runLater(() -> {
                        onSelect(item, pharmaDrug);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    protected void onSelect(PharmaDrugNameDTO item, PharmaDrugDTO pharmaDrug){

    }

    protected void onCancel(){

    }

}
