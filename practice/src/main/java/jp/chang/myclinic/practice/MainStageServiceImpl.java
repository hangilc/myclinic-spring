package jp.chang.myclinic.practice;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.function.Consumer;

public class MainStageServiceImpl implements MainStageService {

    private final Stage stage;
    private Consumer<String> onTitleChangeHandler = t -> {};

    public MainStageServiceImpl(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void setTitle(String title) {
        String curr = stage.getTitle();
        stage.setTitle(title);
        if( !Objects.equals(curr, title) ){
            onTitleChangeHandler.accept(title);
        }
    }

    @Override
    public void setOnTitleChange(Consumer<String> handler) {
        this.onTitleChangeHandler = handler;
    }
}
