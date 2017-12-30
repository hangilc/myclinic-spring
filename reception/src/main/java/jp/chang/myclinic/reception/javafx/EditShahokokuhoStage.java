package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditShahokokuhoStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);

    public EditShahokokuhoStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditShahokokuhoScene.fxml"));
            Parent root = loader.load();
            EditShahokokuhoSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("新規社保国保入力");
            setScene(scene);
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
