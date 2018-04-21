package jp.chang.myclinic.pharma.javafx.pharmadrug;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.IyakuhinMasterDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class IyakuhinListDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(IyakuhinListDialog.class);

    IyakuhinListDialog(List<IyakuhinMasterDTO> masters) {
        HBox root = new HBox(4);
        root.getStylesheets().add("Pharma.css");
        root.getStyleClass().add("iyakuhin-list-dialog");
        ListView<IyakuhinMasterDTO> listView = createListView(masters);
        root.getChildren().addAll(
                listView,
                createCommands(listView)
        );
        setScene(new Scene(root));
    }

    private ListView<IyakuhinMasterDTO> createListView(List<IyakuhinMasterDTO> masters) {
        ListView<IyakuhinMasterDTO> view = new ListView<>();
        view.setCellFactory(listView -> new ListCell<IyakuhinMasterDTO>() {
            @Override
            protected void updateItem(IyakuhinMasterDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("");
                } else {
                    setText(item.name);
                }
            }
        });
        view.getItems().setAll(masters);
        view.setOnMouseClicked(event -> {
            if( event.getClickCount() == 2 ){
                doSelect(view);
            }
        });
        return view;
    }

    private Node createCommands(ListView<IyakuhinMasterDTO> listView){
        HBox hbox = new HBox(4);
        Button selectButton = new Button("選択");
        Button cancelButton = new Button("キャンセル");
        selectButton.setOnAction(evt -> doSelect(listView));
        cancelButton.setOnAction(evt -> onCancel());
        hbox.getChildren().addAll(selectButton, cancelButton);
        return hbox;
    }

    private void doSelect(ListView<IyakuhinMasterDTO> listView){
        IyakuhinMasterDTO master = listView.getSelectionModel().getSelectedItem();
        if( master != null ){
            onSelect(master);
        }
    }

    protected void onSelect(IyakuhinMasterDTO master){

    }

    protected void onCancel(){

    }

}
