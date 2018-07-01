package jp.chang.myclinic.reception.javafx;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

class ImageDisplayStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ImageDisplayStage.class);

    ImageDisplayStage(Path imagePath) {
        setTitle(imagePath.toString());
        Image image = new Image(imagePath.toUri().toString());
        ImageView imageView = new ImageView(image);
        VBox root = new VBox(4);
        root.getChildren().add(imageView);
        setScene(new Scene(root));
    }

}
