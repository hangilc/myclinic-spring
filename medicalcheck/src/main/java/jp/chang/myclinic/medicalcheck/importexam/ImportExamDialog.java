package jp.chang.myclinic.medicalcheck.importexam;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportExamDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ImportExamDialog.class);
    private TextArea textArea = new TextArea();
    private List<BloodExamSpec> examSpecs;

    protected ImportExamDialog(List<BloodExamSpec> examSpecs) {
        this.examSpecs = examSpecs;
        setTitle("血液検査結果のインポート");
        Pane root = createRoot();
        root.getStylesheets().add("MedicalCheck.css");
        root.getStyleClass().add("import-exam-dialog");
        setScene(new Scene(root));
    }

    private Pane createRoot() {
        VBox vbox = new VBox(4);
        vbox.getChildren().addAll(
                new Label("血液検査結果"),
                textArea,
                createCommands()
        );
        return vbox;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> doEnter(textArea.getText()));
        cancelButton.setOnAction(evt -> close());
        hbox.getChildren().addAll(
                enterButton,
                cancelButton
        );
        return hbox;
    }

    private void doEnter(String text){
        Map<String, String> map = new HashMap<>();
        String[] lines = text.split("\\r?\\n");
        for(String line: lines){
            String[] parts = line.split("\\s+", 2);
            if( parts.length >= 2 ){
                String key = parts[0];
                String value = parts[1];
                map.put(key, value);
            }
        }
        List<String> input = new ArrayList<>();
        examSpecs.forEach(spec -> {
            String value = map.get(spec.getName());
            if( value != null ){
                input.add(String.format("%s | %s | %s", spec.getLabel(), value, spec.getUnit()));
            }
        });
        onEnter(input);
    }

    public void onEnter(List<String> input){

    }

}
