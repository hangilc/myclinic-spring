package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.ShahokokuhoDTO;

public class EnterShahokokuhoStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EnterShahokokuhoStage.class);

    public EnterShahokokuhoStage() {
        setTitle("新規社保国保入力");
        Parent root = createMainPane();
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-shahokokuho-stage");
        setScene(new Scene(root));
    }

    private Parent createMainPane(){
        VBox root = new VBox(4);
        EnterShahokokuhoBinder binder = new EnterShahokokuhoBinder();
        binder.setCallbacks(new EnterShahokokuhoBinder.Callbacks() {
            @Override
            public void onEnter(ShahokokuhoDTO shahokokuho) {

            }

            @Override
            public void onCancel() {
                close();
            }
        });
        Node form = binder.getPane();
        root.getChildren().addAll(form);
        return root;
    }

}
