package jp.chang.myclinic.practice.javafx.parts.inplaceediting;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class InPlaceEditable<M> extends StackPane {

    private static Logger logger = LoggerFactory.getLogger(InPlaceEditable.class);

    public interface Editable<M> {
        void setOnEditCallback(Consumer<Editor<M>> onEditCallback);
        Node asNode();
    }
    public interface Editor<M> {
        void setOnEnterCallback(Consumer<Editable<M>> cb);
        void setOnCancelCallback(Runnable cb);
        Node asNode();
    }
    public InPlaceEditable(Editable<M> editable) {
        setEditable(editable);
    }

    private void setEditable(Editable<M> editable){
        editable.setOnEditCallback(editor -> {
            editor.setOnEnterCallback(this::setEditable);
            editor.setOnCancelCallback(() -> InPlaceEditable.this.getChildren().setAll(editable.asNode()));
            getChildren().setAll(editor.asNode());
        });
        getChildren().setAll(editable.asNode());
    }
}
