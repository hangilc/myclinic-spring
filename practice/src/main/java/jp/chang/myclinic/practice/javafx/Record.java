package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;

import java.util.List;
import java.util.stream.Collectors;

public class Record extends VBox {

    public Record(VisitFull2DTO visit){
        getChildren().addAll(createTitle(visit.visit), createBody(visit));
        setPrefWidth(400);
    }

    private Node createTitle(VisitDTO visit){
        RecordTitle recordTitle = new RecordTitle(visit);
        return recordTitle;
    }

    private Node createBody(VisitFull2DTO visit){
        HBox hbox = new HBox();
        VBox left = new VBox();
        VBox right = new VBox();
        left.prefWidthProperty().bind(widthProperty().divide(2));
        left.setStyle("-fx-padding: 0 5 0 0");
        right.prefWidthProperty().bind(widthProperty().divide(2));
        right.setStyle("=fx-padding: 0 0 0 5");
        hbox.getChildren().addAll(left, right);
        List<RecordText> texts = visit.texts.stream().map(RecordText::new).collect(Collectors.toList());
        left.getChildren().addAll(texts.toArray(new RecordText[]{}));
        right.getChildren().add(new Label("RIGHT"));
        return hbox;
    }
}
