package jp.chang.myclinic.recordbrowser;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.Optional;

class SelectPatientDialog extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(SelectPatientDialog.class);
    private PatientDTO selectedPatient;

    SelectPatientDialog() {
        setTitle("患者選択");
        Pane root = createRoot();
        root.getStyleClass().add("select-patient-dialog");
        root.getStylesheets().add("Main.css");
        setScene(new Scene(root));
    }

    Optional<PatientDTO> getSelectedPatient(){
        return Optional.ofNullable(selectedPatient);
    }

    private Pane createRoot(){
        VBox vbox = new VBox(4);
        ListView<PatientDTO> listView = createList();
        vbox.getChildren().addAll(
                createSearchInput(listView),
                listView,
                createCommands(listView)
        );
        return vbox;
    }

    private Node createSearchInput(ListView<PatientDTO> listView){
        HBox hbox = new HBox(4);
        TextField inputField = new TextField();
        Button searchButton = new Button("検索");
        inputField.setOnAction(evt -> doSearch(inputField, listView));
        searchButton.setOnAction(evt -> doSearch(inputField, listView));
        hbox.getChildren().addAll(inputField, searchButton);
        return hbox;
    }

    private ListView<PatientDTO> createList(){
        ListView<PatientDTO> list = new ListView<>();
        list.setCellFactory(listView -> new ListCell<>(){
            @Override
            protected void updateItem(PatientDTO item, boolean empty) {
                super.updateItem(item, empty);
                if( empty || item == null ){
                    setText("");
                } else {
                    String s = String.format("(%04d) %s %s", item.patientId, item.lastName, item.firstName);
                    setText(s);
                }
            }
        });
        return list;
    }

    private Node createCommands(ListView<PatientDTO> listView){
        HBox hbox = new HBox(4);
        Button selectButton = new Button("選択");
        Button cancelButton = new Button("キャンセル");
        selectButton.setDisable(true);
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
            selectButton.setDisable(newValue == null);
        });
        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if( event.getClickCount() == 2 ){
                doSelect(listView);
            }
        });
        selectButton.setOnAction(evt -> doSelect(listView));
        cancelButton.setOnAction(evt -> {
            selectedPatient =null;
            close();
        });
        hbox.getChildren().addAll(selectButton, cancelButton);
        return hbox;
    }

    private void doSelect(ListView<PatientDTO> listView){
        PatientDTO patient = listView.getSelectionModel().getSelectedItem();
        if( patient != null ){
            selectedPatient = patient;
            close();
        }
    }

    private void doSearch(TextField inputField, ListView<PatientDTO> listView){
        String text = inputField.getText().trim();
        if( !text.isEmpty() ){
            Service.api.searchPatient(text)
                    .thenAccept(result -> Platform.runLater(() ->{
                        listView.getItems().setAll(result);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

}
