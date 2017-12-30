package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchPatientStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(SearchPatientStage.class);

    public SearchPatientStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SearchPatientScene.fxml"));
            Parent root = loader.load();
            SearchPatientSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("患者検索");
            setScene(scene);
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
