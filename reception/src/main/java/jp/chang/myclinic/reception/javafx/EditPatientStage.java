package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditPatientStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(EditPatientStage.class);

    public EditPatientStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditPatientScene.fxml"));
            Parent root = loader.load();
            EditPatientSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("受付");
            setScene(scene);
            System.out.println("EditPatientStage");
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }

    }
}
