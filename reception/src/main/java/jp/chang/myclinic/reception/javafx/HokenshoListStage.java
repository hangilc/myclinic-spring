package jp.chang.myclinic.reception.javafx;

import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

class HokenshoListStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(HokenshoListStage.class);

    static class Model {
        String label;
        String file;

        Model(String label, String file){
            this.label = label;
            this.file = file;
        }
    }

    HokenshoListStage(int patientId, List<Model> models) {
        setTitle("保険証画像リスト（" + patientId + "）");
        VBox root = new VBox(4);
        root.getStyleClass().addAll("dialog-root", "list-hokensho-dialog");
        root.getStylesheets().add("css/Main.css");
        if( models.size() == 0 ){
            root.getChildren().add(new Label("（空白）"));
        } else {
            models.forEach(model -> {
                Hyperlink link = new Hyperlink(model.label);
                link.setOnAction(evt -> doOpenImage(patientId, model.file));
                root.getChildren().add(link);
            });
        }
        setScene(new Scene(root));
    }

    private void doOpenImage(int patientId, String file){
        
    }

}
