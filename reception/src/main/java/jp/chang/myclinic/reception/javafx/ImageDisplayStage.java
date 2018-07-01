package jp.chang.myclinic.reception.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

class ImageDisplayStage extends Stage {

    private static Logger logger = LoggerFactory.getLogger(ImageDisplayStage.class);
    private ImageView imageView;

    ImageDisplayStage(Path imagePath) {
        setTitle(imagePath.toString());
        Image image = new Image(imagePath.toUri().toString());
        this.imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        VBox root = new VBox(4);
        root.getStyleClass().addAll("dialog-root", "image-disp-stage");
        root.getStylesheets().add("css/Main.css");
        ScrollPane scroll = new ScrollPane(imageView);
        scroll.getStyleClass().add("image-scroll");
        root.getChildren().addAll(createToolbox(), scroll);
        setScene(new Scene(root));
    }

    private Node createToolbox(){
        HBox hbox = new HBox(4);
        Hyperlink expandLink = new Hyperlink("拡大");
        Hyperlink shrinkLink = new Hyperlink("縮小");
        expandLink.setOnAction(evt -> doExpandImage());
        hbox.getChildren().addAll(expandLink, shrinkLink);
        return hbox;
    }

    private void doExpandImage(){
        imageView.setScaleX(imageView.getScaleX()*1.2);
        imageView.setScaleY(imageView.getScaleY()*1.2);
    }

}
