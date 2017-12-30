package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditKoureiStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(EditShahokokuhoStage.class);

    public EditKoureiStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditKoureiScene.fxml"));
            Parent root = loader.load();
            EditKoureiSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("新規後期高齢入力");
            setScene(scene);
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
