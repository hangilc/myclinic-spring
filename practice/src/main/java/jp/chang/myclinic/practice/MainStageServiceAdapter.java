package jp.chang.myclinic.practice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MainStageServiceAdapter implements MainStageService {

    @Override
    public void setTitle(String title) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setOnTitleChange(Consumer<String> handler) {
        throw new RuntimeException("not implemented");
    }
}
