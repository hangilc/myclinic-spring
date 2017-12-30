package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiptPreviewStage extends Stage {
    private static Logger logger = LoggerFactory.getLogger(ReceiptPreviewStage.class);

    public ReceiptPreviewStage(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReceiptPreviewScene.fxml"));
            Parent root = loader.load();
            ReceiptPreviewSceneController controller = loader.getController();
            Scene scene = new Scene(root);
            setTitle("会計検索");
            setScene(scene);
        } catch(Exception ex){
            logger.error("failed to start hotline", ex);
            System.exit(1);
        }
    }
}
