package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditKouhiStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(EditKouhiStage.class);

    public EditKouhiStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditKouhiScene.fxml"));
            Parent root = loader.load();
            EditKouhiSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("新規公費負担入力");
            setScene(scene);
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
